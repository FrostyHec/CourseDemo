package org.frosty.server.services.course.analysis;

import lombok.RequiredArgsConstructor;
import org.frosty.server.controller.course.analysis.StudyAnalysisController;
import org.frosty.server.controller.course.analysis.StudyAnalysisController.*;
import org.frosty.server.controller.course.analysis.StudyAnalysisController.CourseStudySituation.*;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.entity.bo.Enrollment;
import org.frosty.server.entity.bo.FileSubmission;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.mapper.course.*;
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

    // 教师获取被预警学生与预警信息
    public StudyAnalysisController.WarningInfoList getAllStudentsWarning(Long cid) {
        List<Assignment> assignments = assignmentMapper.getAssignmentsByCourseId(cid); // 获取所有作业
        List<Enrollment> enrollments = enrollmentMapper.getEnrollmentsByCourseId(cid); // 获取所有学生
        List<UserPublicInfo> students = // 获取所有学生的公开信息
                userMapper.findPublicInfoByIds(List.of(enrollments.stream().map(Enrollment::getStudentId).toArray(Long[]::new)));
        List<Long> studentIds = students.stream().map(UserPublicInfo::getUserId).toList(); // 获取所有学生的id
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
                        warning.setWarningStudent(studentId);
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
                        warning.setWarningStudent(fileSubmission.getStudentId());
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
        // 对于每一个作业
        for (Assignment assignment : assignments) {
            // 检查自己是否提交了作业
            FileSubmission fileSubmission =
                    fileSubmissionMapper.selectSubmissionByAssignmentIdAndStudentId(assignment.getAssignmentId(), userId);
            if (fileSubmission == null) {
                StudyAnalysisController.WarningInfo warning = new StudyAnalysisController.WarningInfo();
                warning.setWarningType(WarningInfo.WarningType.homework_uncompleted);
                warning.setWarningStudent(userId);
                warning.setDescription(new AssignmentContent(assignment));
                warning.setDate(assignment.getCreatedAt());
                warnings.add(warning);
            } else {
                // 检查自己的作业得分是否低于平均分的50%
                double averageScore = fileSubmissionMapper.getAverageScoreByAssignmentId(assignment.getAssignmentId());
                if (averageScore != 0 && fileSubmission.getGainedScore() < averageScore * 0.5) {
                    StudyAnalysisController.WarningInfo warning = new StudyAnalysisController.WarningInfo();
                    warning.setWarningType(WarningInfo.WarningType.low_score);
                    warning.setWarningStudent(userId);
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


    // 教师查看章节学习情况
    public StudyAnalysisController.ChapterStudySituation teacherCheckChapterStudySituation(Long cid, Long userId, Long cpid) {
        Chapter chapter = chapterMapper.selectById(cpid);
        if (chapter == null) {
            throw new IllegalArgumentException("Chapter not found");
        }

        ChapterStudySituation situation = new ChapterStudySituation();
        situation.setChapterType(chapter.getChapterType());

        if (chapter.getChapterType() == Chapter.ChapterType.teaching) {
            List<FileSubmission> submissions = fileSubmissionMapper.getAllSubmissionByChapterId(cpid);
            List<Enrollment> enrollments = enrollmentMapper.getEnrollmentsByCourseId(cid);

            List<TeachingChapter.CompletedStatus> completedStatusList = new ArrayList<>();
            for (Enrollment enrollment : enrollments) {
                boolean isCompleted = submissions.stream()
                        .anyMatch(submission -> submission.getStudentId().equals(enrollment.getStudentId()));
                UserPublicInfo student = userMapper.findPublicInfoById(enrollment.getStudentId());
                completedStatusList.add(new TeachingChapter.CompletedStatus(student, isCompleted));
            }

            TeachingChapter teachingChapter = new TeachingChapter();
            teachingChapter.setUncompletedCount((int) completedStatusList.stream().filter(status -> !status.getIsCompleted()).count());
            teachingChapter.setCompletedStatusList(completedStatusList);

            situation.setData(teachingChapter);
        } else {
            List<FileSubmission> submissions = fileSubmissionMapper.getAllSubmissionByChapterId(cpid);
            List<Enrollment> enrollments = enrollmentMapper.getEnrollmentsByCourseId(cid);

            List<ScoreChapter.CompletedStatus> completedStatusList = new ArrayList<>();
            for (Enrollment enrollment : enrollments) {
                boolean hasSubmission = submissions.stream()
                        .anyMatch(submission -> submission.getStudentId().equals(enrollment.getStudentId()));
                boolean isGraded = submissions.stream()
                        .anyMatch(submission -> submission.getStudentId().equals(enrollment.getStudentId()) && submission.getGainedScore() != null);
                UserPublicInfo student = userMapper.findPublicInfoById(enrollment.getStudentId());
                completedStatusList.add(new ScoreChapter.CompletedStatus(student, hasSubmission, isGraded));
            }

            ScoreChapter scoreChapter = new ScoreChapter();
            scoreChapter.setAverageScore((int) submissions.stream().mapToInt(FileSubmission::getGainedScore).average().orElse(0));
            scoreChapter.setUngradedCount((int) completedStatusList.stream().filter(status -> status.getIsCompleted() && !status.getIsGraded()).count());
            scoreChapter.setUncompletedCount((int) completedStatusList.stream().filter(status -> !status.getIsCompleted()).count());
            scoreChapter.setCompletedStatusList(completedStatusList);

            situation.setData(scoreChapter);
        }

        return situation;
    }

    // 教师查看课程学习情况
    public StudyAnalysisController.CourseStudySituation teacherCheckCourseStudySituation(Long cid, Long userId) {
        List<Chapter> chapters = chapterMapper.getAllChaptersByCourseId(cid);
        CourseStudySituation situation = new CourseStudySituation();

        CourseStudySituation.TeachingChapterInfo teachingInfo = new TeachingChapterInfo();
        ScoringChapterInfo assignmentInfo = new ScoringChapterInfo();
        ScoringChapterInfo projectInfo = new ScoringChapterInfo();
        List<DifficultChapter> difficultChapters = new ArrayList<>();

        int totalTeachingChapters = 0;
        int completedTeachingChapters = 0;
        int totalAssignmentChapters = 0;
        int completedAssignmentChapters = 0;
        int totalProjectChapters = 0;
        int completedProjectChapters = 0;
        int totalStudents = enrollmentMapper.getEnrollmentsByCourseId(cid).size();

        for (Chapter chapter : chapters) {
            List<FileSubmission> submissions = fileSubmissionMapper.getAllSubmissionByChapterId(chapter.getChapterId());

            if (chapter.getChapterType() == Chapter.ChapterType.teaching) {
                totalTeachingChapters++;
                if (submissions.size() == totalStudents) {
                    completedTeachingChapters++;
                }
            } else if (chapter.getChapterType() == Chapter.ChapterType.assignment) {
                totalAssignmentChapters++;
                if (submissions.stream().filter(submission -> submission.getGainedScore() != null).count() == totalStudents) {
                    completedAssignmentChapters++;
                }

                ScoringChapterInfo.SingleChapterInfo singleChapterInfo = new ScoringChapterInfo.SingleChapterInfo(
                        chapter,
                        (int) submissions.stream().mapToInt(FileSubmission::getGainedScore).average().orElse(0),
                        submissions.stream().mapToInt(FileSubmission::getGainedScore).max().orElse(0),
                        (int) submissions.stream().filter(submission -> submission.getGainedScore() != null).count(),
                        totalStudents
                );
                assignmentInfo.getChangingRate().add(singleChapterInfo);
            } else if (chapter.getChapterType() == Chapter.ChapterType.project) {
                totalProjectChapters++;
                if (submissions.stream().filter(submission -> submission.getGainedScore() != null).count() == totalStudents) {
                    completedProjectChapters++;
                }

                ScoringChapterInfo.SingleChapterInfo singleChapterInfo = new ScoringChapterInfo.SingleChapterInfo(
                        chapter,
                        (int) submissions.stream().mapToInt(FileSubmission::getGainedScore).average().orElse(0),
                        submissions.stream().mapToInt(FileSubmission::getGainedScore).max().orElse(0),
                        (int) submissions.stream().filter(submission -> submission.getGainedScore() != null).count(),
                        totalStudents
                );
                projectInfo.getChangingRate().add(singleChapterInfo);
            }

            // Identify difficult chapters (e.g., chapters with low completion rate)
            if (chapter.getChapterType() != Chapter.ChapterType.teaching) {
                double completionRate = (double) submissions.stream().filter(submission -> submission.getGainedScore() != null).count() / totalStudents;
                if (completionRate < 0.5) {
                    DifficultChapter difficultChapter = new DifficultChapter(
                            chapter,
                            new DifficultChapter.DifficultWarnInfo(WarningInfo.WarningType.low_score, completionRate)
                    );
                    difficultChapters.add(difficultChapter);
                }
            }
        }

        teachingInfo.setTotalChapterCount(totalTeachingChapters);
        teachingInfo.setCompletedCount(completedTeachingChapters);
        teachingInfo.setMaxPossibleCompletedCount(totalTeachingChapters * totalStudents);

        assignmentInfo.setTotalChapterCount(totalAssignmentChapters);
        assignmentInfo.setCompletedCount(completedAssignmentChapters);
        assignmentInfo.setMaxPossibleCompletedCount(totalAssignmentChapters * totalStudents);

        projectInfo.setTotalChapterCount(totalProjectChapters);
        projectInfo.setCompletedCount(completedProjectChapters);
        projectInfo.setMaxPossibleCompletedCount(totalProjectChapters * totalStudents);

        situation.setTeaching(teachingInfo);
        situation.setAssignment(assignmentInfo);
        situation.setProject(projectInfo);
        situation.setDifficultChapters(difficultChapters);

        return situation;
    }


}