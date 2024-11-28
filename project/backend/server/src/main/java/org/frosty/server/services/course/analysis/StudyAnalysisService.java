package org.frosty.server.services.course.analysis;

import lombok.RequiredArgsConstructor;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.server.controller.course.analysis.StudyAnalysisController;
import org.frosty.server.controller.course.analysis.StudyAnalysisController.*;
import org.frosty.server.controller.course.analysis.StudyAnalysisController.CourseStudySituation.DifficultChapter;
import org.frosty.server.controller.course.analysis.StudyAnalysisController.CourseStudySituation.ScoringChapterInfo;
import org.frosty.server.controller.course.analysis.StudyAnalysisController.CourseStudySituation.TeachingChapterInfo;
import org.frosty.server.controller.course.progress.CourseProgressController;
import org.frosty.server.entity.bo.*;
import org.frosty.server.entity.bo.progress.ResourceCompleteRecord;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.mapper.course.*;
import org.frosty.server.mapper.course.progress.ChapterCompleteMapper;
import org.frosty.server.mapper.course.progress.CourseCompleteMapper;
import org.frosty.server.mapper.course.progress.ResourceCompleteMapper;
import org.frosty.server.mapper.user.UserMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StudyAnalysisService {
    //    private final CourseMapper courseMapper;
    private final AssignmentMapper assignmentMapper;
    private final EnrollmentMapper enrollmentMapper;
    private final ChapterMapper chapterMapper;
    private final FileSubmissionMapper fileSubmissionMapper;
    private final UserMapper userMapper;
    private final ResourceCompleteMapper resourceCompleteMapper;
    private final CourseCompleteMapper courseCompleteMapper;
    private final ResourceMapper resourceMapper;
    private final ChapterCompleteMapper chapterCompleteMapper;

    // 教师获取被预警学生与预警信息
    public StudyAnalysisController.WarningInfoList getAllStudentsWarning(Long cid) {
        List<Assignment> assignments = assignmentMapper.getAssignmentsByCourseId(cid); // 获取所有作业
        List<Enrollment> enrollments = enrollmentMapper.getEnrollmentsByCourseId(cid); // 获取所有学生
        List<Long> studentIds = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            studentIds.add(enrollment.getStudentId());
        }
        List<UserPublicInfo> userPublicInfos = userMapper.findPublicInfoByIds(studentIds);
        // 学生id -> 公开信息
        Map<Long, UserPublicInfo> userPublicInfoMap = new HashMap<>();
        for (UserPublicInfo userPublicInfo : userPublicInfos) {
            userPublicInfoMap.put(userPublicInfo.getUserId(), userPublicInfo);
        }
        List<StudyAnalysisController.WarningInfo> warnings = new ArrayList<>(); // 预警信息

        // course -> chapter -> assignment -> fileSubmission
        //      -> enrollment
        // 对于每一个作业
        for (Assignment assignment : assignments) {
            List<FileSubmission> fileSubmissions = fileSubmissionMapper.getAllSubmission(assignment.getAssignmentId()); // 获取所有提交的作业
            // 获取所有未提交作业的学生
            studentIds.stream()
                    .filter(studentId -> fileSubmissions.stream().noneMatch(fs -> fs.getStudentId().equals(studentId)))
                    .forEach(studentId -> {
                        StudyAnalysisController.WarningInfo warning = new StudyAnalysisController.WarningInfo();
                        warning.setWarningType(WarningInfo.WarningType.homework_uncompleted);
                        warning.setWarningStudent(userPublicInfoMap.get(studentId));
                        warning.setDescription(new AssignmentContent(assignment));
                        warning.setDate(assignment.getCreatedAt());
                        warnings.add(warning);
                    });
            // 获取所有提交作业的平均分
            double averageScore = fileSubmissions.stream()
                    .mapToDouble(FileSubmission::getGainedScore)
                    .average()
                    .orElse(0);
            // 获取所有低分学生
            fileSubmissions.stream()
                    .filter(fileSubmission -> fileSubmission.getGainedScore() < averageScore * 0.5)
                    .forEach(fileSubmission -> {
                        StudyAnalysisController.WarningInfo warning = new StudyAnalysisController.WarningInfo();
                        warning.setWarningType(WarningInfo.WarningType.low_score);
                        warning.setWarningStudent(userPublicInfoMap.get(fileSubmission.getStudentId()));
                        warning.setDescription(new AssignmentContent(assignment));
                        warning.setDate(assignment.getCreatedAt());
                        warnings.add(warning);
                    });
        }
        return new StudyAnalysisController.WarningInfoList(warnings);
    }

    // 学生获取自己在一门课程下的预警信息
    public StudyAnalysisController.WarningInfoList getMyWarning(Long cid, Long userId) {
        List<Assignment> assignments = assignmentMapper.getAssignmentsByCourseId(cid); // 获取这门课程中的所有作业
        List<StudyAnalysisController.WarningInfo> warnings = new ArrayList<>(); // 预警信息
        UserPublicInfo user = userMapper.findPublicInfoById(userId); // 获取自己的公开信息
        // 对于每一个作业
        for (Assignment assignment : assignments) {
            // 检查自己是否提交了作业
            FileSubmission fileSubmission =
                    fileSubmissionMapper.selectSubmissionByAssignmentIdAndStudentId(assignment.getAssignmentId(), userId);
            if (fileSubmission == null) {
                StudyAnalysisController.WarningInfo warning = new StudyAnalysisController.WarningInfo();
                warning.setWarningType(WarningInfo.WarningType.homework_uncompleted);
                warning.setWarningStudent(user);
                warning.setDescription(new AssignmentContent(assignment));
                warning.setDate(assignment.getCreatedAt());
                warnings.add(warning);
            } else {
                // 检查自己的作业得分是否低于平均分的50%
                double averageScore = fileSubmissionMapper.getAverageScoreByAssignmentId(assignment.getAssignmentId());
                if (averageScore != 0 && fileSubmission.getGainedScore() < averageScore * 0.5) {
                    StudyAnalysisController.WarningInfo warning = new StudyAnalysisController.WarningInfo();
                    warning.setWarningType(WarningInfo.WarningType.low_score);
                    warning.setWarningStudent(user);
                    warning.setDescription(new AssignmentContent(assignment));
                    warning.setDate(assignment.getCreatedAt());
                    warnings.add(warning);
                }
            }
        }
        return new StudyAnalysisController.WarningInfoList(warnings);
    }

    // 教师获取所有学生的成绩表
    public StudyAnalysisController.StudentsScoreTable getAllStudentsScore(Long cid, Long userId) {
        List<Assignment> assignments = assignmentMapper.getAssignmentsByCourseId(cid); // 获取所有作业
        List<Enrollment> enrollments = enrollmentMapper.getEnrollmentsByCourseId(cid); // 获取所有学生
        List<UserPublicInfo> students = // 获取所有学生的公开信息
                userMapper.findPublicInfoByIds(List.of(enrollments.stream().map(Enrollment::getStudentId).toArray(Long[]::new)));

        List<Assignment> column;//第M次作业，按照createdTime排序
        List<UserPublicInfo> row;//student N
        List<List<Integer>> data = new ArrayList<>(); // data[N][M]
        List<Integer> totalScore = new ArrayList<>(); // N row,each student 1
        for (int i = 0; i < students.size(); i++) {
            totalScore.add(0);
        }
        List<Integer> averageScore = new ArrayList<>(); // M column, each assignment 1
        int averageTotalScore;

        column = assignments.stream()
                .sorted(Comparator.comparing(Assignment::getCreatedAt))
                .toList();
        row = students;
        for (Assignment assignment : column) {
            // 获取所有提交的作业
            List<FileSubmission> fileSubmissions = fileSubmissionMapper.getAllSubmission(assignment.getAssignmentId());
            // 学生 -> 作业得分 map
            Map<UserPublicInfo, Integer> studentScores = fileSubmissions.stream()
                    .collect(Collectors.toMap(fs -> students.stream().filter(s -> s.getUserId().equals(fs.getStudentId())).findFirst().orElse(null),
                            FileSubmission::getGainedScore));
            // 作业得分列表
            List<Integer> scores = new ArrayList<>();
            for (int i = 0; i < students.size(); i++) {
                UserPublicInfo student = students.get(i); // 获取学生
                scores.add(Objects.requireNonNullElse(studentScores.get(student), 0)); // 0 代表未提交
                totalScore.set(i, totalScore.get(i) + scores.get(i));  // 计算总分
            }

            data.add(scores);
            averageScore.add(scores.stream().mapToInt(Integer::intValue).sum() / scores.size());
        }
        averageTotalScore = totalScore.stream().reduce(Integer::sum).orElse(0) / totalScore.size();

        return new StudentsScoreTable(column, row, data, totalScore, averageScore, averageTotalScore);
    }

    // 学生获取自己的成绩表
    public StudyAnalysisController.AssignmentChapterSituationList getMyScore(Long cid, Long userId) {
        // 找到课程的所有章节
        List<Chapter> chapters = chapterMapper.getAllChaptersByCourseId(cid);
        List<Assignment> assignments = assignmentMapper.getAssignmentsByCourseId(cid); // 获取所有作业

        // 创建章节映射，将章节 ID 映射到章节信息
        Map<Long, Chapter> chapterMap = chapters.stream()
                .collect(Collectors.toMap(Chapter::getChapterId, chapter -> chapter));

        List<AssignmentChapterSituation> assignmentChapterSituations = new ArrayList<>();

        // 创建一个 Map 来存储每个章节的作业情况
        Map<Long, List<AssignmentSituation>> chapterToAssignmentsSituations = new HashMap<>();

        // 对于每一个作业
        for (Assignment assignment : assignments) {
            Long chapterId = assignment.getChapterId();
            Chapter chapter = chapterMap.get(chapterId);

            if (chapter == null) {
                continue; // 如果章节不存在，跳过
            }

            // 获取作业的提交情况
            FileSubmission fileSubmission = fileSubmissionMapper.selectSubmissionByAssignmentIdAndStudentId(assignment.getAssignmentId(), userId);

            // 获取作业的评分情况
            boolean isGraded = fileSubmission != null && fileSubmission.getGainedScore() != null;
            AssignmentSituation assignmentSituation = new AssignmentSituation(
                    assignment,
                    fileSubmission != null,
                    isGraded,
                    isGraded ? fileSubmission.getGainedScore() : null
            );

            // 如果当前章节还没有记录在 map 中，则初始化一个列表
            chapterToAssignmentsSituations.computeIfAbsent(chapterId, k -> new ArrayList<>()).add(assignmentSituation);
        }

        // 构建最终的 AssignmentChapterSituation 列表
        for (Chapter chapter : chapters) {
            Long chapterId = chapter.getChapterId();
            Chapter.ChapterType chapterType = chapter.getChapterType();
            String chapterName = chapter.getChapterTitle();

            // 获取当前章节的作业情况
            List<AssignmentSituation> assignmentsSituations = chapterToAssignmentsSituations.getOrDefault(chapterId, new ArrayList<>());

            // 判断该章节是否所有作业都已经评分
            boolean isAllGraded = assignmentsSituations.stream().allMatch(AssignmentSituation::getIsGraded);

            assignmentChapterSituations.add(new AssignmentChapterSituation(
                    chapterId,
                    chapterType,
                    chapterName,
                    isAllGraded,
                    assignmentsSituations
            ));
        }

        return new AssignmentChapterSituationList(assignmentChapterSituations);
    }


    /**
     * 教师查看章节学习情况
     *
     * @param cid    课程ID
     * @param userId 教师的用户ID
     * @param cpid   章节ID
     * @return 一个 {@link StudyAnalysisController.ChapterStudySituation} 对象，包含章节的学习情况。
     * 返回的对象包括：
     * <ul>
     *     <li>{@link Chapter.ChapterType} chapterType - 章节类型（教学、作业或项目）。</li>
     *     <li>{@link StudyAnalysisController.ChapterStudySituationData} data - 详细的学习情况数据，可能是：
     *         <ul>
     *             <li>{@link StudyAnalysisController.TeachingChapter} 对于教学章节，包含：
     *                 <ul>
     *                     <li>uncompletedCount - 未完成章节的学生数。</li>
     *                     <li>completedStatusList - 一个 {@link StudyAnalysisController.TeachingChapter.CompletedStatus} 对象列表，每个对象代表一个学生的完成情况。</li>
     *                 </ul>
     *             </li>
     *             <li>{@link StudyAnalysisController.ScoreChapter} 对于作业或项目章节，包含：
     *                 <ul>
     *                     <li>averageScore - 章节的平均分。</li>
     *                     <li>ungradedCount - 未评分的学生数。</li>
     *                     <li>uncompletedCount - 未完成章节的学生数。</li>
     *                     <li>completedStatusList - 一个 {@link StudyAnalysisController.ScoreChapter.CompletedStatus} 对象列表，每个对象代表一个学生的完成和评分情况。</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *     </li>
     * </ul>
     */
    public ChapterStudySituation teacherCheckChapterStudySituation(Long cid, Long userId, Long cpid) {
        // 1. 获取章节基本信息
        Chapter chapter = chapterMapper.selectById(cpid);
        if (chapter == null) {
            throw new IllegalArgumentException("Chapter not found");
        }

        // 2. 获取所有选课学生
        List<Enrollment> enrollments = enrollmentMapper.getEnrollmentsByCourseId(cid);
        List<Long> studentIds = enrollments.stream()
                .map(Enrollment::getStudentId)
                .collect(Collectors.toList());
        List<UserPublicInfo> students = userMapper.findPublicInfoByIds(studentIds);

        // 3. 创建返回对象
        ChapterStudySituation situation = new ChapterStudySituation();
        situation.setChapterType(chapter.getChapterType());

        if (chapter.getChapterType() == Chapter.ChapterType.teaching) {
            // 3.1 教学章节分析
            // 获取所有完成记录
            List<Map<String, Object>> completionRecords =
                    chapterCompleteMapper.getCompletionStatuses(cpid, studentIds);

            // 将完成记录转换为map便于查找
            Map<Long, Boolean> completionStatusMap = completionRecords.stream()
                    .collect(Collectors.toMap(
                            status -> ((Number) status.get("student_id")).longValue(),
                            status -> (Boolean) status.get("is_completed")
                    ));

            // 构建学生完成状态列表
            List<TeachingChapter.CompletedStatus> completedStatusList = students.stream()
                    .map(student -> new TeachingChapter.CompletedStatus(
                            student,
                            completionStatusMap.getOrDefault(student.getUserId(), false)
                    ))
                    .collect(Collectors.toList());

            // 创建教学章节数据对象
            TeachingChapter teachingData = new TeachingChapter();
            teachingData.setUncompletedCount((int) completedStatusList.stream()
                    .filter(status -> !status.getIsCompleted())
                    .count());
            teachingData.setCompletedStatusList(completedStatusList);

            situation.setData(teachingData);

        } else {
            // 3.2 作业/项目章节分析
            List<FileSubmission> submissions =
                    fileSubmissionMapper.getAllSubmissionByChapterId(cpid);

            List<ScoreChapter.CompletedStatus> completedStatusList = new ArrayList<>();

            // 统计每个学生的完成情况
            for (UserPublicInfo student : students) {
                boolean hasSubmission = submissions.stream()
                        .anyMatch(sub -> sub.getStudentId().equals(student.getUserId()));
                boolean isGraded = submissions.stream()
                        .anyMatch(sub -> sub.getStudentId().equals(student.getUserId())
                                && sub.getGainedScore() != null);

                completedStatusList.add(new ScoreChapter.CompletedStatus(
                        student, hasSubmission, isGraded));
            }

            // 创建评分章节数据对象
            ScoreChapter scoreData = new ScoreChapter();

            double averageScore = submissions.stream()
                    .filter(s -> s.getGainedScore() != null)
                    .mapToInt(FileSubmission::getGainedScore)
                    .average()
                    .orElse(0);

            scoreData.setAverageScore((int) averageScore);
            scoreData.setUngradedCount((int) completedStatusList.stream()
                    .filter(status -> status.getIsCompleted() && !status.getIsGraded())
                    .count());
            scoreData.setUncompletedCount((int) completedStatusList.stream()
                    .filter(status -> !status.getIsCompleted())
                    .count());
            scoreData.setCompletedStatusList(completedStatusList);

            situation.setData(scoreData);
        }

        return situation;
    }

    /**
     * 教师查看课程学习情况
     *
     * @param cid    课程ID
     * @param userId 教师的用户ID
     * @return 一个 {@link StudyAnalysisController.CourseStudySituation} 对象，包含课程的学习情况。
     * 返回的对象包括：
     * <ul>
     *     <li>{@link StudyAnalysisController.CourseStudySituation.TeachingChapterInfo} teaching - 教学章节信息，包含：
     *         <ul>
     *             <li>totalChapterCount - 教学章节总数。</li>
     *             <li>completedCount - 已完成的教学章节数。</li>
     *             <li>maxPossibleCompletedCount - 最大可能完成的章节数（学生数 * 章节数）。</li>
     *         </ul>
     *     </li>
     *     <li>{@link StudyAnalysisController.CourseStudySituation.ScoringChapterInfo} assignment - 作业章节信息，包含：
     *         <ul>
     *             <li>totalChapterCount - 作业章节总数。</li>
     *             <li>completedCount - 已完成的作业章节数。</li>
     *             <li>maxPossibleCompletedCount - 最大可能完成的章节数（学生数 * 章节数）。</li>
     *             <li>changingRate - 章节完成率变化列表，每个元素是一个 {@link StudyAnalysisController.CourseStudySituation.ScoringChapterInfo.SingleChapterInfo} 对象，包含：
     *                 <ul>
     *                     <li>chapter - 章节信息。</li>
     *                     <li>averageScore - 平均分。</li>
     *                     <li>maxScore - 最高分。</li>
     *                     <li>completedCount - 已完成的学生数。</li>
     *                     <li>maxCompletedCount - 最大可能完成的学生数。</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *     </li>
     *     <li>{@link StudyAnalysisController.CourseStudySituation.ScoringChapterInfo} project - 项目章节信息，包含：
     *         <ul>
     *             <li>totalChapterCount - 项目章节总数。</li>
     *             <li>completedCount - 已完成的项目章节数。</li>
     *             <li>maxPossibleCompletedCount - 最大可能完成的章节数（学生数 * 章节数）。</li>
     *             <li>changingRate - 章节完成率变化列表，每个元素是一个 {@link StudyAnalysisController.CourseStudySituation.ScoringChapterInfo.SingleChapterInfo} 对象，包含：
     *                 <ul>
     *                     <li>chapter - 章节信息。</li>
     *                     <li>averageScore - 平均分。</li>
     *                     <li>maxScore - 最高分。</li>
     *                     <li>completedCount - 已完成的学生数。</li>
     *                     <li>maxCompletedCount - 最大可能完成的学生数。</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *     </li>
     *     <li>difficultChapters - 难度较大的章节列表，每个元素是一个 {@link StudyAnalysisController.CourseStudySituation.DifficultChapter} 对象，包含：
     *         <ul>
     *             <li>chapter - 章节信息。</li>
     *             <li>warningInfo - 预警信息，包含：
     *                 <ul>
     *                     <li>type - 预警类型。</li>
     *                     <li>description - 完成百分比。</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *     </li>
     * </ul>
     */
    public CourseStudySituation teacherCheckCourseStudySituation(Long cid, Long userId) {
        // 1. 获取课程所有章节
        List<Chapter> chapters = chapterMapper.getAllChaptersByCourseId(cid);
        List<Long> students = enrollmentMapper.getEnrollmentsByCourseId(cid).stream().map(Enrollment::getStudentId).toList();
        int totalStudents = students.size();

        // 2. 初始化返回对象
        CourseStudySituation situation = new CourseStudySituation();
        TeachingChapterInfo teachingInfo = new TeachingChapterInfo();
        ScoringChapterInfo assignmentInfo = new ScoringChapterInfo();
        ScoringChapterInfo projectInfo = new ScoringChapterInfo();
        List<DifficultChapter> difficultChapters = new ArrayList<>();

        // 3. 初始化计数器
        int[] counts = new int[6]; // 存储各类型章节的统计数据
        assignmentInfo.setChangingRate(new ArrayList<>());
        projectInfo.setChangingRate(new ArrayList<>());

        // 4. 分析每个章节
        for (Chapter chapter : chapters) {
            List<FileSubmission> submissions =
                    fileSubmissionMapper.getAllSubmissionByChapterId(chapter.getChapterId());

            switch (chapter.getChapterType()) {
                case teaching -> processTeachingChapter(
                        chapter, students, totalStudents, counts);
                case assignment -> processAssignmentChapter(
                        chapter, submissions, totalStudents, counts,
                        assignmentInfo, difficultChapters);
                case project -> processProjectChapter(
                        chapter, submissions, totalStudents, counts,
                        projectInfo, difficultChapters);
            }
        }

        // 5. 设置统计结果
        setChapterInfos(teachingInfo, assignmentInfo, projectInfo,
                counts, totalStudents);

        // 6. 构建返回对象
        situation.setTeaching(teachingInfo);
        situation.setAssignment(assignmentInfo);
        situation.setProject(projectInfo);
        situation.setDifficultChapters(difficultChapters);

        return situation;
    }

    // 处理教学章节统计
    private void processTeachingChapter(Chapter chapter, List<Long> students,
                                        int totalStudents, int[] counts) {
        counts[0]++; // totalTeachingChapters
        // 检查学生是否完成了该章节下的资源
        List<Resource> resources = resourceMapper.getAll(chapter.getChapterId());
        List<Resource> videoResource = resources.parallelStream().filter(r -> r.getResourceType() == Resource.ResourceType.video).toList();
        // 获取所有资源并过滤出视频资源
        List<Long> resourceIds = resourceMapper.getAll(chapter.getChapterId()).parallelStream()
                .filter(r -> r.getResourceType() == Resource.ResourceType.video)
                .map(Resource::getResourceId)
                .collect(Collectors.toList());

        // 获取完成状态
        List<ResourceCompleteRecord> completionStatuses = resourceCompleteMapper.getCompletionStatuses(resourceIds, students);

        // 将完成状态映射到学生资源完成情况的 Map 中
        Map<Long, Set<Long>> studentResourceCompletionMap = completionStatuses.parallelStream()
                .collect(Collectors.groupingBy(
                        ResourceCompleteRecord::getStudentId,
                        Collectors.mapping(ResourceCompleteRecord::getResourceId, Collectors.toSet())
                ));

        // 检查每个学生是否完成了所有视频资源
        long completedCount = students.parallelStream()
                .filter(studentId -> videoResource.parallelStream()
                        .allMatch(r -> studentResourceCompletionMap.getOrDefault(studentId, Collections.emptySet()).contains(r.getResourceId()))
                )
                .count();

        // 更新计数
        counts[1] += (int) completedCount; // completedTeachingChapters
    }

    // 处理作业章节统计
    private void processAssignmentChapter(Chapter chapter, List<FileSubmission> submissions,
                                          int totalStudents, int[] counts, ScoringChapterInfo assignmentInfo,
                                          List<DifficultChapter> difficultChapters) {
        counts[2]++; // totalAssignmentChapters
        processChapterScoring(chapter, submissions, totalStudents, counts, 2,
                assignmentInfo, difficultChapters);
    }

    // 处理项目章节统计
    private void processProjectChapter(Chapter chapter, List<FileSubmission> submissions,
                                       int totalStudents, int[] counts, ScoringChapterInfo projectInfo,
                                       List<DifficultChapter> difficultChapters) {
        counts[4]++; // totalProjectChapters
        processChapterScoring(chapter, submissions, totalStudents, counts, 4,
                projectInfo, difficultChapters);
    }

    // 处理评分章节的通用逻辑
    private void processChapterScoring(Chapter chapter, List<FileSubmission> submissions,
                                       int totalStudents, int[] counts, int baseIndex,
                                       ScoringChapterInfo info, List<DifficultChapter> difficultChapters) {
        // 计算已得分的学生数
        long gradedCount = submissions.parallelStream()
                .filter(s -> s.getGainedScore() != null)
                .count();
        counts[baseIndex + 1] += (int) gradedCount;

        // 计算已完成的学生数
        long submittedCount = submissions.size();

        // 计算章节统计信息
        ScoringChapterInfo.SingleChapterInfo chapterInfo = new ScoringChapterInfo.SingleChapterInfo(
                chapter,
                (int) submissions.parallelStream().filter(s -> s.getGainedScore() != null).mapToInt(FileSubmission::getGainedScore).average().orElse(0),
                submissions.parallelStream().filter(s -> s.getGainedScore() != null).mapToInt(FileSubmission::getGainedScore).max().orElse(0),
                (int) gradedCount,
                totalStudents
        );
        info.getChangingRate().add(chapterInfo);

        // 检查是否是困难章节中的“低完成率”章节
        double completionRate = (double) submittedCount / totalStudents;
        if (completionRate < 0.5) {
            difficultChapters.add(new DifficultChapter(
                    chapter,
                    new DifficultChapter.DifficultWarnInfo(
                            WarningInfo.WarningType.homework_uncompleted,
                            completionRate
                    )
            ));
        }

        // 检查是否是困难章节中的“低平均分”章节
        double averageScore = submissions.parallelStream()
                .filter(s -> s.getGainedScore() != null)
                .mapToInt(FileSubmission::getGainedScore)
                .average()
                .orElse(0);
        if (averageScore < 60 && gradedCount > 0) { // 低于 60 分, 且老师已经开始评分
            difficultChapters.add(new DifficultChapter(
                    chapter,
                    new DifficultChapter.DifficultWarnInfo(
                            WarningInfo.WarningType.low_score,
                            averageScore
                    )
            ));
        }
    }

    // 设置章节信息统计结果
    private void setChapterInfos(TeachingChapterInfo teachingInfo,
                                 ScoringChapterInfo assignmentInfo, ScoringChapterInfo projectInfo,
                                 int[] counts, int totalStudents) {

        teachingInfo.setTotalChapterCount(counts[0]);
        teachingInfo.setCompletedCount(counts[1]);
        teachingInfo.setMaxPossibleCompletedCount(counts[0] * totalStudents);

        assignmentInfo.setTotalChapterCount(counts[2]);
        assignmentInfo.setCompletedCount(counts[3]);
        assignmentInfo.setMaxPossibleCompletedCount(counts[2] * totalStudents);

        projectInfo.setTotalChapterCount(counts[4]);
        projectInfo.setCompletedCount(counts[5]);
        projectInfo.setMaxPossibleCompletedCount(counts[4] * totalStudents);
    }


}