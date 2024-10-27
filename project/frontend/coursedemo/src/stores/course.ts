import { computed, reactive, ref, watch, type Ref } from 'vue'
import { defineStore } from 'pinia'
import { CourseStatus, getCourseCall, Publication, type CourseEntity } from '@/api/course/CourseAPI'
import { ChapterType, getAllChapterCall, type ChapterEntity } from '@/api/course/ChapterAPI'
import { ResourceType, type ResourceEntity } from '@/api/course/CourseResourceAPI'
import { useRoute } from 'vue-router'
export interface AllInOneEntity {
  course_info: CourseEntity,
  chapters: {
    chapter_info: ChapterEntity, 
    resourses: ResourceEntity[],
  }[],
}

export interface UnifyTree {
  label: string,
  children: UnifyTree[],

  order: number,
  description: string,

  data: CourseEntity|ChapterEntity|ResourceEntity,
}

function unify(data: CourseEntity | ChapterEntity | ResourceEntity): UnifyTree {
  if('course_name' in data) {
    return {
      label: data.course_name, children: [],
      order: 0, description: data.description,
      data: data,
    }
  }
  if('chapter_title' in data) {
    return {
      label: data.chapter_title, children: [],
      order: data.chapter_order, description: data.content,
      data: data,
    }
  }
  if('resource_name' in data) {
    return {
      label: data.resource_name, children: [],
      order: data.resource_order, description: '',
      data: data,
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

function build(course: AllInOneEntity): UnifyTree {
  const root = unify(course.course_info);
  course.chapters.forEach((chapter) => {
    const sub_root = unify(chapter.chapter_info)
    chapter.resourses.forEach((resource) => sub_root.children.push(unify(resource)))
    root.children.push(sub_root)
  })
  return root
}

export function path_convert(path: undefined|string|string[]): string[] {
  if(!path)
    return []
  if(typeof path === 'string')
    return [path]
  return path
}

async function get_all(course_id: number): Promise<AllInOneEntity|undefined> {
  return undefined
}

export const useCourseStore = defineStore('course', () => {
  let course_data: AllInOneEntity|undefined = {
    course_info: {
      course_id: 0,
      course_name: 'OOAD (Fall2024)', 
      description: 'Welcome to this course!!!',
      teacher_id: 0,
      status: CourseStatus.archived,
      publication: Publication.open,
      created_at: new Date,
      updated_at: new Date('2077-04-01'),
    },
    chapters: [{
      chapter_info: {
        chapter_id: 0,
        course_id: 0,
        chapter_order: 0,
        chapter_title: 'Chapter 1',
        chapter_type: ChapterType.assignment,
        content: 'Let us start!!!',
        created_at: new Date,
        updated_at: new Date('2077-04-02'),
      },
      resourses: [{
        resource_id: 0,
        chapter_id: 0,
        resource_name: 'slide',
        suffix: 'md',
        file_name: 'abc',
        resource_order: 0,
        resource_version_name: 'init',
        resource_version_order: 0,
        resource_type: ResourceType.courseware,
        student_can_download: true,
        created_at: new Date,
        updated_at: new Date('2077-04-03'),
      }, {
        resource_id: 1,
        chapter_id: 0,
        resource_name: 'tag',
        suffix: 'zip',
        file_name: 'funny',
        resource_order: 0,
        resource_version_name: 'init',
        resource_version_order: 0,
        resource_type: ResourceType.attachment,
        student_can_download: true,
        created_at: new Date,
        updated_at: new Date('2077-04-03'),
      },],
    }, {
      chapter_info: {
        chapter_id: 0,
        course_id: 0,
        chapter_order: 0,
        chapter_title: 'Chapter 2',
        chapter_type: ChapterType.assignment,
        content: 'Let us continue!!!',
        created_at: new Date,
        updated_at: new Date('2077-04-11'),
      },
      resourses: [{
        resource_id: 2,
        chapter_id: 0,
        resource_name: 'slide',
        suffix: 'md',
        file_name: 'abc',
        resource_order: 0,
        resource_version_name: 'init',
        resource_version_order: 0,
        resource_type: ResourceType.courseware,
        student_can_download: true,
        created_at: new Date,
        updated_at: new Date('2077-04-23'),
      }, {
        resource_id: 3,
        chapter_id: 0,
        resource_name: 'tag',
        suffix: 'zip',
        file_name: 'funny',
        resource_order: 0,
        resource_version_name: 'init',
        resource_version_order: 0,
        resource_type: ResourceType.attachment,
        student_can_download: true,
        created_at: new Date,
        updated_at: new Date('2077-04-13'),
      },],
    },],
  }
  const unify_course_data = ref<Array<UnifyTree>>([])
  const current_data: Ref<UnifyTree|undefined> = ref(undefined)
  
  function current_course_id(): number|undefined {
    return course_data?.course_info.course_id
  }
  async function load(course_id: number, l: string[], reload: boolean) {
    if(reload || current_course_id()!==course_id)
      course_data = await get_all(course_id)
    if(typeof course_data === 'undefined') {
      unify_course_data.value = []
      current_data.value = undefined
      return
    }
    unify_course_data.value = [build(course_data)]
    current_data.value = unify_course_data.value[0]

    const labels = l.map((label) => label.replace(/-/g, ' '))
    if(labels.length==0)
      return

    let flag = false 
    for(const i of current_data.value.children)
      if(i.label==labels[0]) {
        flag = true
        current_data.value = i
      }
    if(!flag) {
      current_data.value = undefined
      return
    }
    if(labels.length==1)
      return

    flag = false
    for(const i of current_data.value.children)
      if(i.label==labels[1]) {
        flag = true
        current_data.value = i
      }
    if(!flag) {
      current_data.value = undefined
      return
    }
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
  }

  const route = useRoute()
  const watch_current_route = watch(() => {return {id: route.params.course_id, labels: route.params.labels}}, 
    async (new_data) => {
      console.log(new_data)
      await load_from_route(false)
    }
  )

  return {course_data, current_course_id, unify_course_data, current_data, load_from_route}
})