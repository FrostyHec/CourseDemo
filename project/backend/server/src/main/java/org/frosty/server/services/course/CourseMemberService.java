package org.frosty.server.services.course;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.Course;
import org.frosty.server.entity.bo.Enrollment;
import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.po.StudentWithEnrollStatus;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.mapper.course.CourseMapper;
import org.frosty.server.mapper.course.EnrollmentMapper;
import org.frosty.server.mapper.user.UserMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseMemberService {
    private final EnrollmentMapper enrollmentMapper;
    private final CourseMapper courseMapper;
    private final UserMapper userMapper;

    /**
     * 学生申请加入课程。
     * <p>
     * 该方法通过学生的操作，将学生加入指定课程。学生的状态将设置为 "publik"。
     * </p>
     *
     * @param id 课程的唯一标识符（courseId）。
     */
    public void enrollStudentsToCourse(Long id, Long studentId) {
        Enrollment enrollments = new Enrollment().setCourseId(id).
                setStudentId(studentId).setStatus(Enrollment.EnrollmentType.publik);
        enrollmentMapper.insert(enrollments);
    }

    /**
     * 邀请学生加入指定课程。
     * <p>
     * 该方法通过教师的操作，批量将学生邀请到指定课程。学生的状态将设置为 "invited"。
     * </p>
     *
     * @param courseId    课程的唯一标识符（courseId）。
     * @param studentList 要邀请的学生ID列表，每个ID代表一个学生。
     */
    public void inviteStudentsToCourse(Long courseId, List<Long> studentList) {
        if (studentList != null && !studentList.isEmpty()) {
            enrollmentMapper.inviteStudentsToCourse(courseId, studentList);
        }
    }

    /**
     * 获取指定课程的学生列表，包含学生的公开信息和入课状态。
     * <p>
     * 该方法根据课程ID查询所有加入该课程的学生，并返回学生的公开信息和他们在该课程中的状态。支持分页查询。
     * 如果 pageSize 为 -1，则返回所有学生。
     * </p>
     *
     * @param courseId 课程的唯一标识符（courseId），用于查询该课程中的学生。
     * @param pageNum  当前的分页页码，从 1 开始。如果 pageSize 为 -1，则忽略此参数。
     * @param pageSize 每页显示的记录数。如果为 -1，则返回所有符合条件的学生，不进行分页。
     * @return 包含学生列表的 List 对象，每个元素为 `StudentWithEnrollStatus`，其中包含学生的公开信息和入课状态。
     */
    public List<StudentWithEnrollStatus> getStudentList(Long courseId, int pageNum, int pageSize) {
        QueryWrapper<Enrollment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);

        List<Enrollment> enrollments;

        // 检查 pageSize 是否为 -1，如果是则查询所有记录
        if (pageSize == -1) {
            enrollments = enrollmentMapper.selectList(queryWrapper);
        } else {
            Page<Enrollment> page = new Page<>(pageNum, pageSize);
            Page<Enrollment> selectedPage = enrollmentMapper.selectPage(page, queryWrapper);
            enrollments = selectedPage.getRecords();
        }

        // 提取所有学生的ID
        List<Long> studentIds = enrollments.stream()
                .map(Enrollment::getStudentId)
                .collect(Collectors.toList());
        if(studentIds.isEmpty()){
            return List.of();
        }
        List<User> users = userMapper.selectBatchIds(studentIds);  // 批量查询

        // 创建一个 Map 来存储 userId -> User 的映射，方便后续查找
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getUserId, user -> user));

        // 构造结果列表

        return enrollments.stream().map(enrollment -> {
            // 从 userMap 中获取对应的 User 对象
            User user = userMap.get(enrollment.getStudentId());
            StudentWithEnrollStatus studentWithEnrollStatus = new StudentWithEnrollStatus();
            UserPublicInfo userPublicInfo = new UserPublicInfo();
            userPublicInfo.setEmail(user.getEmail());
            userPublicInfo.setUserId(user.getUserId());
            userPublicInfo.setRole(user.getRole());
            userPublicInfo.setLastName(user.getLastName());
            userPublicInfo.setFirstName(user.getFirstName());
            studentWithEnrollStatus.setStudent(userPublicInfo);
            studentWithEnrollStatus.setStatus(enrollment.getStatus());
            return studentWithEnrollStatus;
        }).toList();
    }


    /**
     * 获取指定学生加入的课程列表，并支持分页查询。
     * <p>
     * 该方法根据学生的唯一标识符（studentId）查询该学生加入的课程列表。如果 pageSize 为 -1，
     * 则返回该学生加入的所有课程；否则，按分页查询返回指定页码的课程。
     * 返回的结果以 Map 形式包含课程列表，结构如下：
     * <pre>
     * {
     *     "content": [
     *         { Course 对象的具体结构省略 },
     *         ...
     *     ]
     * }
     * </pre>
     * </p>
     *
     * @param studentId 学生的唯一标识符，用于查询该学生加入的课程。
     * @param pageNum   当前的分页页码，从 1 开始。如果 pageSize 为 -1，则忽略此参数。
     * @param pageSize  每页显示的记录数。如果为 -1，则返回所有符合条件的记录，不进行分页。
     * @return 包含课程列表的 Map 对象，键为 "content"，值为课程的 List。
     */
    public List<Course> getStudentCourses(Long studentId, int pageNum, int pageSize) {
        QueryWrapper<Enrollment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId);
        return getTargetCourse(pageNum, pageSize, queryWrapper);
    }

    /**
     * 获取指定老师加入的课程列表，并支持分页查询。
     * <p>
     * 该方法根据老师的唯一标识符（studentId）查询该老师主讲的课程列表。如果 pageSize 为 -1，
     * 则返回该主讲加入的所有课程；否则，按分页查询返回指定页码的课程。
     * 返回的结果以 Map 形式包含课程列表，结构如下：
     * <pre>
     * {
     *     "content": [
     *         { Course 对象的具体结构省略 },
     *         ...
     *     ]
     * }
     * </pre>
     * </p>
     *
     * @param teacherId 老师的唯一标识符，用于查询该老师主讲的课程。
     * @param pageNum   当前的分页页码，从 1 开始。如果 pageSize 为 -1，则忽略此参数。
     * @param pageSize  每页显示的记录数。如果为 -1，则返回所有符合条件的记录，不进行分页。
     * @return 课程列表。
     */
    public List<Course> getTeacherCourses(Long teacherId, int pageNum, int pageSize) {
        return courseMapper.selectTeacherCourses(teacherId,pageNum,pageSize);
    }

    /**
     * 获取指定学生在某课程中的状态。
     * <p>
     * 该方法根据课程ID和学生ID，查询该学生在指定课程中的状态。状态结果通过
     * Map 的形式返回，结构如下：
     * <pre>
     * {
     *     "status": "状态值"
     * }
     * </pre>
     * </p>
     *
     * @param courseId  课程的唯一标识符，用于查询该课程中的学生状态。
     * @param studentId 学生的唯一标识符，用于查询该学生在课程中的状态。
     * @return 包含学生状态的 Map 对象，键为 "status"，值为状态的名称。
     */
    public Map<String, String> getStudentStatus(Long courseId, Long studentId) {
        Map<String, String> result = new HashMap<>();
        QueryWrapper<Enrollment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId);
        queryWrapper.eq("course_id", courseId);
        Enrollment enrollment = enrollmentMapper.selectOne(queryWrapper);
        if (enrollment != null) {
            result.put("status", enrollment.getStatus().name());
        }
        return result;
    }

    public List<Course> getSubmittedCourses(int page_num, int page_size) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", Course.CourseStatus.submitted.name());
        return courseMapper.selectList(queryWrapper);
    }

    @NotNull
    private List<Course> getTargetCourse(int pageNum, int pageSize, QueryWrapper<Enrollment> queryWrapper) {
        List<Long> courseIds;

        if (pageSize == -1) {
            // 查询所有符合条件的记录
            List<Enrollment> enrollments = enrollmentMapper.selectList(queryWrapper);
            courseIds = enrollments.stream()
                    .map(Enrollment::getCourseId)
                    .collect(Collectors.toList());
            // 返回所有课程信息
        } else {
            // 创建分页对象
            Page<Enrollment> page = new Page<>(pageNum, pageSize);
            // 查询分页数据
            Page<Enrollment> selectedPage = enrollmentMapper.selectPage(page, queryWrapper);
            courseIds = selectedPage.getRecords()
                    .stream()
                    .map(Enrollment::getCourseId)
                    .toList();
        }
        // 返回分页课程信息
        return courseMapper.selectBatchIds(courseIds);
    }

    public List<Course> getHandledCourse(int pageNum, int pageSize) {
        return courseMapper.selectHandledCourse(pageNum,pageSize);
    }
}
