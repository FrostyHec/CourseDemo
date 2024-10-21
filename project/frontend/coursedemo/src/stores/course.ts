import { ref } from 'vue'
import { defineStore } from 'pinia'
import { ChapterType, CourseStatus, type AllInOneEntity } from '@/api/CourseAPI'

export const useCourseStore = defineStore('course', () => {
  const course_data: AllInOneEntity = {
    course_info: {
      course_id: 0, 
      course_name: 'OOAD (Fall2024)', 
      description: 'Wellcome to this course!!!',
      teacher_id: 0,
      status: CourseStatus.archived,
      create_at: new Date,
      update_at: new Date('2077-04-01'),
    },
    chapters: [{
        chapter_info: {
          chapter_id: 0,
          chapter_title: 'Chapter 1',
          chapter_order: 0,
          chapter_type: ChapterType.assignment,
          
        }
      }
    ]
  }
})