import { computed, reactive, ref, watch, type Ref } from 'vue'
import { defineStore } from 'pinia'
import { CourseStatus, getCourseCall, Publication, type CourseEntity } from '@/api/course/CourseAPI'
import { ChapterType, getAllChapterCall, getChapterCall, type ChapterEntity } from '@/api/course/ChapterAPI'
import { getResourcesByChapterCall, ResourceType, type ResourceEntity, type ResourceWithAccessKey } from '@/api/course/CourseResourceAPI'
import { useRoute } from 'vue-router'
import { checkCourseProgressCall, completeChapterCall, completeCourseCall, completeResourceCall } from '@/api/course/CourseProgressAPI'
import { ElMessage } from 'element-plus'

export interface ResourceEntityPlus extends ResourceEntity {
  access_key: string
}
export function pack_resource(data: ResourceEntityPlus): ResourceWithAccessKey {
  return {
    resource: data,
    access_key: data.access_key
  }
}
export function unpack_resource(data: ResourceWithAccessKey): ResourceEntityPlus {
  return {...data.resource, access_key: data.access_key}
}

export interface AllInOneEntity {
  course_info: CourseEntity,
  chapters: {
    chapter_info: ChapterEntity, 
    resources: {
      top: ResourceEntityPlus,
      vers: ResourceEntityPlus[],
    }[],
  }[],
}

export interface UnifyTree {
  label: string,
  children: UnifyTree[],
  id: number,

  order: number,
  description: string,
  complete: boolean,

  data: CourseEntity|ChapterEntity|ResourceEntityPlus,
}

let course_complete = false
const chapter_complete = new Set<number>()
const no_video_chapter = new Set<number>()
const resource_complete = new Set<number>()
function unify(data: CourseEntity|ChapterEntity|ResourceEntityPlus, key: number, is_top: boolean = true): UnifyTree {
  if('course_name' in data) {
    return {
      label: data.course_name, children: [], id: key,
      order: 0, description: data.description,
      data: data, complete: course_complete
    }
  }
  if('chapter_title' in data) {
    return {  
      label: data.chapter_title, children: [], id: key,
      order: data.chapter_order, description: data.content,
      data: data, complete: chapter_complete.has(data.chapter_id)
    }
  }
  if('resource_name' in data) {
    return {
      label: is_top ? data.resource_name : data.resource_version_name, children: [], id: key,
      order: is_top ? data.resource_order : data.resource_version_order, description: '',
      data: data, complete: resource_complete.has(data.resource_id)
    }
  }
  return data //never
}

function unify_update(node: UnifyTree): boolean {
  if('course_name' in node.data) {
    node.data.course_name = node.label
    node.data.description = node.description
  }
  if('chapter_title' in node.data) {
    node.data.chapter_title = node.label
    node.data.chapter_order = node.order
    node.data.content = node.description
  }
  if('resource_name' in node.data) {
    node.data.resource_name = node.label
    node.data.resource_order = node.order
  }
  return true
}


export function path_convert(path: undefined|string|string[]): string[] {
  if(!path)
    return []
  if(typeof path === 'string')
    return [path]
  return path
}

export async function get_all(course_id: number): Promise<AllInOneEntity|undefined> {
  const course_info = (await getCourseCall(course_id)).data
  const chapter_list = (await getAllChapterCall(course_id)).data.content.sort((a, b) => a.chapter_order - b.chapter_order)
  const chapter_all_list: {
    chapter_info: ChapterEntity, 
    resources: {
      top: ResourceEntityPlus,
      vers: ResourceEntityPlus[],
    }[]
  }[] = []
  for(const chapter_info of chapter_list) {
    const resource_list = (await getResourcesByChapterCall(chapter_info.chapter_id))
      .data.content
      .map(unpack_resource)
      .sort((a, b) => {
        if(a.resource_order===b.resource_order)
          return a.resource_version_order - b.resource_version_order
        return a.resource_order - b.resource_order
      })
    const resource_all_list: {top: ResourceEntityPlus, vers: ResourceEntityPlus[]}[] = []
    let current: {top: ResourceEntityPlus, vers: ResourceEntityPlus[]} | undefined = undefined
    for(const resource of resource_list) {
      if(current===undefined) {
        current = {top: resource, vers: []}
      }
      else {
        if(current.top.resource_order==resource.resource_order)
          current.vers.push(resource)
        else {
          resource_all_list.push(current)
          current = current = {top: resource, vers: []}
        }
      }
    }
    if(current!==undefined)
      resource_all_list.push(current)
    chapter_all_list.push({chapter_info: chapter_info, resources: resource_all_list})
  }
  console.log({course_info: course_info, chapters: chapter_all_list})
  return {course_info: course_info, chapters: chapter_all_list}
}

export const useCourseStore = defineStore('course', () => {
  let course_data: AllInOneEntity|undefined = undefined
  const unify_course_data = ref<Array<UnifyTree>>([])
  const current_data: Ref<UnifyTree|undefined> = ref(undefined)
  const default_open = ref([0])
  
  function current_course_id(): number|undefined {
    return course_data?.course_info.course_id
  }

  function current_course_teacher(): number|undefined {
    return course_data?.course_info.teacher_id
  }

  const ver_resource_set :Set<number> = new Set()
  function build(course: AllInOneEntity): UnifyTree {
    ver_resource_set.clear()
    let cnt = 0
    default_open.value = [0]
    return course.chapters.reduce<UnifyTree>((root, chapter) => {
      default_open.value.push(cnt)
      root.children.push(
        chapter.resources.reduce<UnifyTree>((sub_root, resource) => {
          sub_root.children.push(
            resource.vers.reduce<UnifyTree>((subsub_root, ver_resource) => {
              subsub_root.children.push(unify(ver_resource, cnt++, false))
              ver_resource_set.add(ver_resource.resource_id)
              return subsub_root
            }, unify(resource.top, cnt++))
          )
          return sub_root
        }, unify(chapter.chapter_info, cnt++))
      )
      return root
    }, unify(course.course_info, cnt++))
  }

  async function load_complete() {
    const id = current_course_id()
    if(id===undefined) return
    const msg = await checkCourseProgressCall(id)
    if(msg.code!==200) {
      ElMessage({
        message: 'Get progress network error',
        type: 'error',
      })
    }
    console.log('check progress', msg.data)
    const course_pro = msg.data
    course_complete = course_pro.is_completed
    chapter_complete.clear()
    resource_complete.clear()
    no_video_chapter.clear()
    for(const c of course_pro.chapter_progress) {
      if(c.is_completed)
        chapter_complete.add(c.chapter_id)
      if(c.video_resources.length==0)
        no_video_chapter.add(c.chapter_id)
      for(const r of c.video_resources)
        if(r.is_completed)
          resource_complete.add(r.resource_id)
    }
  }

  async function load(course_id: number, l: string[], reload: boolean) {
    if(reload || current_course_id()!==course_id)
      course_data = await get_all(course_id)
    if(typeof course_data === 'undefined') {
      unify_course_data.value = []
      current_data.value = undefined
      return
    }
    await load_complete()
    unify_course_data.value = [build(course_data)]
    current_data.value = unify_course_data.value[0]

    const labels = l.map((label) => label.replace(/-/g, ' '))
    if(labels.length==0)
      return

    let father_id = 0
    for(const label of labels) {
      let flag = false
      if(!current_data.value)
        return
      for(const sub of current_data.value.children)
        if(sub.label==label) {
          flag = true
          father_id = current_data.value.id
          current_data.value = sub
          break
        }
      if(!flag) {
        if('resource_name' in current_data.value.data && 
           current_data.value.data.resource_version_name==label)
           return
        current_data.value = undefined
        return
      }
    }
    if(current_data.value) {
      default_open.value.push(father_id)
      if('chapter_title' in current_data.value.data && 
         no_video_chapter.has(current_data.value.data.chapter_id)) {
        await completeChapterCall(current_data.value.data.chapter_id)
        current_data.value.complete = true
        let course_complete = true
        for(const c of unify_course_data.value[0].children)
          if(!c.complete) {
            course_complete = false
            return
          }
        const course_id = current_course_id()
        if(course_complete && course_id!==undefined)
          await completeCourseCall(course_id)
      }
    }
  }

  const breadcrumb = ref<{key: number, label: string, link?: string}[]>([])
  function generate_breadcrumb(id: string|string[], s: string[]) {
    if(id===undefined || !course_data)
      return []
    let prefix = '/course' + '/' + id
    const res = []
    if(s.length==0)
      res.push({key: 0, label: course_data.course_info.course_name})
    else
      res.push({key: 0, label: course_data.course_info.course_name, link: prefix})
    for(let i=0;i<s.length;i++) {
      prefix += '/' + s[i]
      if(i==s.length-1) {
        res.push({key: i+1, label: s[i].replace(/-/g, ' ')})
      } else {
        res.push({key: i+1, label: s[i].replace(/-/g, ' '), link: prefix})
      }
    }
    breadcrumb.value = res
  }
  async function load_from_route(reload: boolean) {
    const id = route.params.course_id
    const labels = route.params.labels
    if(!id) {
      unify_course_data.value = []
      current_data.value = undefined
      return
    }
    if(labels==='')
      await load(Number(id), [], reload)
    else
      await load(Number(id), path_convert(labels), reload)
    generate_breadcrumb(id, path_convert(labels))
  }

  const route = useRoute()
  const watch_current_route = watch(() => {return {id: route.params.course_id, labels: route.params.labels}}, 
    async (new_data) => {
      console.log(new_data)
      await load_from_route(false)
    },
    {immediate: true}
  )

  async function complete_resource(resource_id: number) {
    await completeResourceCall(resource_id)
    const id = current_course_id()
    if(id===undefined) return
    const msg = await checkCourseProgressCall(id)
    if(msg.code!==200) {
      ElMessage({
        message: 'Get progress network error',
        type: 'error',
      })
    }
    const course_pro = msg.data
    let current_chapter: number|undefined = undefined
    for(const chapter_pro of course_pro.chapter_progress) {
      let finish = false
      for(const resource_pro of chapter_pro.video_resources) {
        if(resource_pro.resource_id===resource_id) {
          finish = true
          let chapter_complete = true
          if(chapter_pro.is_completed)
            break
          current_chapter = chapter_pro.chapter_id
          for(const res of chapter_pro.video_resources)
            if(!res.is_completed && !ver_resource_set.has(res.resource_id)) {
              chapter_complete = false
              break
            }
          if(chapter_complete)
            await completeChapterCall(chapter_pro.chapter_id)
          break
        }
      }
      if(finish)
        break
    }
    if(current_chapter===undefined || course_pro.is_completed)
      return
    let course_complete = true
    for(const chapter_pro of course_pro.chapter_progress)
      if(chapter_pro.chapter_id!==current_chapter || !chapter_pro.is_completed)
          course_complete = false
    if(course_complete)
      await completeCourseCall(course_pro.course_id)
    await load_from_route(true)
  }

  return {
    current_course_id, current_course_teacher, 
    unify_course_data, current_data, 
    load_from_route, default_open, breadcrumb,
    complete_resource,
  }
})