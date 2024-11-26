package org.frosty.server.controller.course.analysis;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import org.frosty.auth.annotation.GetPassedToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.services.course.analysis.StudyAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(PathConstant.API + "/course/{cid}/study/analysis")
@RequiredArgsConstructor
public class StudyAnalysisController {
    private final StudyAnalysisService studyAnalysisService;

    @GetMapping("/students-warning")
    public WarningInfoList getAllStudentsWarning(@GetPassedToken AuthInfo auth, @PathVariable Long cid) {
        //教师获取被预警学生与预警信息
        return studyAnalysisService.getAllStudentsWarning(cid);
    }

    @GetMapping("/my-warning")
    public WarningInfoList getMyWarning(@GetPassedToken AuthInfo auth, @PathVariable Long cid) {
        //学生获取自己的预警信息
        Long userId = auth.getUserID();
        return studyAnalysisService.getMyWarning(cid, userId);
    }

    @GetMapping("/students-score")
    public StudentsScoreTable getAllStudentsScore(@GetPassedToken AuthInfo auth, @PathVariable Long cid) {
        //教师获取所有学生的成绩表
        Long userId = auth.getUserID();
        return studyAnalysisService.getAllStudentsScore(cid, userId);
    }

    @GetMapping("/my-score")
    public AssignmentChapterSituationList getMyScore(@GetPassedToken AuthInfo auth, @PathVariable Long cid) {
        //学生获取自己的成绩表
        Long userId = auth.getUserID();
        return studyAnalysisService.getMyScore(cid, userId);
    }

    @GetMapping("/teacher/chapter/{cpid}")
    public ChapterStudySituation teacherCheckChapterStudySituation(@GetPassedToken AuthInfo auth,
                                                                   @PathVariable Long cid, @PathVariable Long cpid) {
        //教师查看章节学习情况
        Long userId = auth.getUserID();

        return studyAnalysisService.teacherCheckChapterStudySituation(cid, userId, cpid);
    }

    @GetMapping("/teacher/course")
    public CourseStudySituation teacherCheckCourseStudySituation(@GetPassedToken AuthInfo auth,
                                                                 @PathVariable Long cid) {
        //教师查看课程学习情况
        Long userId = auth.getUserID();
        return studyAnalysisService.teacherCheckCourseStudySituation(cid, userId);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = TeachingChapter.class, name = "teaching"),
            @JsonSubTypes.Type(value = ScoreChapter.class, name = "score")
    })
    public interface ChapterStudySituationData {
    }

    // 添加 JsonTypeInfo 和 JsonSubTypes 注解
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = AssignmentContent.class, name = "assignment_content")
    })
    public interface WarningInfoContent {
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseStudySituation {
        private TeachingChapterInfo teaching;
        private ScoringChapterInfo assignment;//注意这里面章节信息都只是assignment类型的
        private ScoringChapterInfo project;//注意这里面章节信息一定都只是project类型的
        private List<DifficultChapter> difficultChapters;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class TeachingChapterInfo {
            private Integer totalChapterCount; // 教学章节数
            private Integer completedCount; // 已完成的学生数
            private Integer maxPossibleCompletedCount;//stuCnt*chapters
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ScoringChapterInfo {
            private Integer totalChapterCount; // 作业/项目章节数
            private Integer completedCount; // 已完成的学生数
            private Integer maxPossibleCompletedCount;//stuCnt*chapters
            private List<SingleChapterInfo> changingRate; //章节完成率变化

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class SingleChapterInfo {
                private Chapter chapter; // 章节信息
                private Integer averageScore; // 平均分
                private Integer maxScore; // 最高分
                private Integer completedCount; // 已完成的学生数
                private Integer maxCompletedCount; // stuCnt*cpters
            }
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class DifficultChapter {
            private Chapter chapter; // 章节信息
            private DifficultWarnInfo warningInfo; // 预警信息

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class DifficultWarnInfo {
                private WarningInfo.WarningType type; // 预警类型
                private Double description; //完成百分比
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChapterStudySituation {
        private Chapter.ChapterType chapterType;
        private ChapterStudySituationData data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TeachingChapter implements ChapterStudySituationData {
        private Integer uncompletedCount; // 未完成的学生数
        private List<CompletedStatus> completedStatusList; //学生完成情况

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class CompletedStatus {
            private UserPublicInfo student; //学生信息
            private Boolean isCompleted; //是否完成
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScoreChapter implements ChapterStudySituationData { //assignment or project
        private Integer averageScore; // 平均分
        private Integer ungradedCount; // 未打分的学生数
        private Integer uncompletedCount; // 未完成的学生数
        private List<CompletedStatus> completedStatusList; // 学生完成情况

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class CompletedStatus {
            private UserPublicInfo student; // 学生信息
            private Boolean isCompleted; // 是否完成
            private Boolean isGraded; // 是否打分
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignmentChapterSituationList {
        private List<AssignmentChapterSituation> content; //章节列表
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignmentChapterSituation {
        private Long chapterId; //章节ID
        private Chapter.ChapterType chapterType; //章节类型
        private String chapterName; //章节名称
        private Boolean isAllGraded; //是否全部打分
        private List<AssignmentSituation> assignments; //作业列表
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignmentSituation {
        private Assignment assignment; //作业信息
        private Boolean hasSubmission; //是否有提交
        private Boolean isGraded; //是否打分
        private Integer receivedScores; //得分
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentsScoreTable {
        private List<Assignment> column;//第M次作业，按照createdTime排序
        private List<UserPublicInfo> row;//student N
        private List<List<Integer>> data; // data[N][M]
        private List<Integer> totalScore; // N row,each student 1
        private List<Integer> averageScore; // M column, each assignment 1
        private Integer averageTotalScore;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WarningInfoList {
        private List<WarningInfo> content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WarningInfo {
        private WarningType warningType;
        private UserPublicInfo warningStudent;
        private WarningInfoContent description;
        private OffsetDateTime date;

        @Getter
        @AllArgsConstructor
        public enum WarningType {
            homework_uncompleted(AssignmentContent.class),
            low_score(AssignmentContent.class);
            private final Class<?> clazz;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignmentContent implements WarningInfoContent {
        private Assignment assignmentEntity;
    }
}
