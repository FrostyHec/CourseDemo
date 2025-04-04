import { reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import type Node from 'element-plus/es/components/tree/src/model/node'
import { CourseStatus, createCourseCall, Publication, updateCourseInfoCall, type CourseEntity } from '@/api/course/CourseAPI'
import { ChapterType, createChapterCall, updateChapterCall, type ChapterEntity } from '@/api/course/ChapterAPI'
import { ResourceType, updateResourceMetadataCall, uploadResourceCall, type ResourceEntity } from '@/api/course/CourseResourceAPI'
import { type ResourceEntityPlus, type UnifyTree } from './course'
import { AssignmentType, createAssignmentCall, updateAssignmentCall, type AssignmentEntity } from '@/api/course/AssignmentAPI'


export const useFormStore = defineStore('form', () => {
  
  const course_visibility = ref(false)
  const chapter_visibility = ref(false)
  const resource_visibility = ref(false)
  const assignment_visibility = ref(false)

  const course_null: CourseEntity = {
    course_id: 0, 
    course_name: '', 
    description: '',
    teacher_id: 0,
    status: CourseStatus.creating,
    publication: undefined,
    created_at: undefined,
    updated_at: undefined,
  } as any
  const chapter_null: ChapterEntity = {
    chapter_id: 0,
    course_id: 0,
    chapter_order: 0,
    chapter_title: '',
    chapter_type: undefined,
    content: '',
    created_at: undefined,
    updated_at: undefined,
    visible: true,
    publication: true,
  } as any
  const resource_null: ResourceEntityPlus = {
    resource_id: 0,
    chapter_id: 0,
    resource_name: '',
    suffix: '',
    file_name: '',
    resource_order: 0,
    resource_version_name: 'v0.0',
    resource_version_order: 0,
    resource_type: undefined,
    student_can_download: true,
    created_at: undefined,
    updated_at: undefined,
    access_key: '',
  } as any
  const assignment_null: AssignmentEntity = {
    assignment_id: 0,
    chapter_id: 0,
    description: '',
    assignment_name: '',
    assignment_type: AssignmentType.file_upload,
    allow_update_submission: true,
    latest_submission_time: new Date(),
    maximum_score: 0,
    allow_student_to_view_score: true,
    created_at: undefined,
    updated_at: undefined
  } as any

  const course_form = ref<CourseEntity>(course_null)
  const chapter_form = ref<ChapterEntity>(chapter_null)
  const resource_form = ref<ResourceEntityPlus>(resource_null)
  const assignment_form = ref<AssignmentEntity>(assignment_null)

  const node = ref<Node|undefined>(undefined)
  const mode = ref<'Add'|'Edit'>('Add')

  const resource_mode = ref<'init'|'new_version'>('init')

  let data_save: CourseEntity|ChapterEntity|ResourceEntityPlus|AssignmentEntity|undefined = undefined 
  function open_form(data: CourseEntity|ChapterEntity|ResourceEntityPlus|AssignmentEntity, set_mode?: 'Add'|'Edit') {
    console.log(check_name.value)
    if(set_mode) {
      data_save = {...data}
      mode.value = set_mode
    }
    if('course_name' in data) {
      course_form.value = {...data}
      course_visibility.value = true
    }
    if('chapter_title' in data) {
      chapter_form.value = {...data}
      chapter_visibility.value = true
    }
    if('resource_name' in data) {
      resource_form.value = {...data}
      resource_visibility.value = true
    }
    if('assignment_type' in data) {
      assignment_form.value = {...data}
      assignment_visibility.value = true
    }
  }
  function reset_form() {
    if(data_save)
      open_form(data_save)
  }

  const check_name = ref<string[]>([])
  async function modify_course(): Promise<boolean> {
    let msg
    if(mode.value=='Add')
      msg = await createCourseCall(course_form.value)
    else
      msg = await updateCourseInfoCall(course_form.value.course_id, course_form.value)
    return msg.code==200
  }
  async function modify_chapter(): Promise<boolean> {
    let msg
    if(mode.value=='Add')
      msg = await createChapterCall(chapter_form.value.course_id, chapter_form.value)
    else
      msg = await updateChapterCall(chapter_form.value.chapter_id, chapter_form.value)
    return msg.code==200
  }
  async function modify_resource(file?: File): Promise<number|undefined> {
    if(mode.value=='Add') {
      if(!file)
        return undefined
      const msg = await uploadResourceCall(resource_form.value.chapter_id, resource_form.value, file)
      return msg.code==200 ? msg.data.resource_id : undefined
    }
    else {
      const msg = await updateResourceMetadataCall(resource_form.value.resource_id, resource_form.value)
      return msg.code==200 ? 1 : undefined
    }
  }
  async function modify_assignment(): Promise<boolean> {
    let msg
    if(mode.value=='Add')
      msg = await createAssignmentCall(assignment_form.value.chapter_id, assignment_form.value)
    else
      msg = await updateAssignmentCall(assignment_form.value.assignment_id, assignment_form.value)
    return msg.code==200
  }

  return {
    course_visibility, chapter_visibility, resource_visibility, assignment_visibility,
    course_form, chapter_form, resource_form, assignment_form,
    course_null, chapter_null, resource_null, assignment_null,
    modify_course, modify_chapter, modify_resource, modify_assignment,
    open_form, reset_form,
    node, mode, resource_mode, check_name,
  }
})