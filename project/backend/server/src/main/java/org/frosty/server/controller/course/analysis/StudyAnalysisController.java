package org.frosty.server.controller.course.analysis;

import lombok.*;
import org.frosty.auth.annotation.GetPassedToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.entity.bo.Chapter;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(PathConstant.API+ "/course/{cid}/study/analysis")
@RequiredArgsConstructor
public class StudyAnalysisController {
    @GetMapping("/students-warning")
    public WarningInfoList getAllStudentsWarning(@GetPassedToken AuthInfo auth, @PathVariable Long cid){
        FrameworkUtils.notImplemented();
        return null;
    }

    @GetMapping("/my-warning")
    public WarningInfoList getMyWarning(@GetPassedToken AuthInfo auth, @PathVariable Long cid){
        FrameworkUtils.notImplemented();
        return null;
    }
    @GetMapping("/students-score")
    public StudentsScoreTable getAllStudentsScore(@GetPassedToken AuthInfo auth, @PathVariable Long cid){
        FrameworkUtils.notImplemented();
        return null;
    }
    @GetMapping("/my-score")
    public AssignmentChapterSituationList getMyScore(@GetPassedToken AuthInfo auth,@PathVariable Long cid){
        FrameworkUtils.notImplemented();
        return null;
    }
    @GetMapping("/teacher/chapter/{cpid}")
    public ChapterStudySituation teacherCheckChapterStudySituation(@GetPassedToken AuthInfo auth,
                                                                       @PathVariable Long cid, @PathVariable Long cpid){
        FrameworkUtils.notImplemented();
        return null;
    }

    @GetMapping("/teacher/course")
    public CourseStudySituation teacherCheckCourseStudySituation(@GetPassedToken AuthInfo auth,
                                                                            @PathVariable Long cid, @PathVariable Long cpid){
        FrameworkUtils.notImplemented();
        return null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CourseStudySituation{
        private TeachingChapterInfo teaching;
        private ScoringChapterInfo assignment;//注意这里面章节信息都只是assignment类型的
        private ScoringChapterInfo project;//注意这里面章节信息一定都只是project类型的
        private List<DifficultChapter> difficultChapters;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class TeachingChapterInfo{
            private Integer totalChapterCount;
            private Integer completedCount;
            private Integer maxPossibleCompletedCount;//stuCnt*chapters
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ScoringChapterInfo{
            private Integer totalChapterCount;
            private Integer completedCount;
            private Integer maxPossibleCompletedCount;//stuCnt*chapters
            private List<SingleChapterInfo> changingRate;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            private static class SingleChapterInfo{
                private Chapter chapter;
                private Integer averageScore;
                private Integer maxScore;
                private Integer completedCount;
                private Integer maxCompletedCount;//stuCnt*cpters
            }
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class DifficultChapter{
            private Chapter chapter;
            private DifficultWarnInfo warningInfo;
            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            public static class DifficultWarnInfo{
                private WarningInfo.WarningType type;
                private Double description;//完成百分比
            }
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChapterStudySituation{
        private Chapter.ChapterType chapterType;
        private ChapterStudySituationData data;
    }

    public interface ChapterStudySituationData{
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TeachingChapter implements ChapterStudySituationData {
        private Integer uncompletedCount;
        private List<CompletedStatus> completedStatusList;
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class CompletedStatus{
            private UserPublicInfo student;
            private Boolean isCompleted;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScoreChapter implements ChapterStudySituationData {
        private Integer averageScore;
        private Integer ungradedCount;
        private Integer uncompletedCount;
        private List<CompletedStatus> completedStatusList;
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class CompletedStatus{
            private UserPublicInfo student;
            private Boolean isCompleted;
            private Boolean isGraded;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignmentChapterSituationList{
        private List<AssignmentChapterSituation> content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignmentChapterSituation{
        private Long chapterId;
        private Chapter.ChapterType chapterType;
        private String chapterName;
        private Boolean isAllGraded;
        private List<AssignmentSituation> assignments;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignmentSituation{
        private Assignment assignment;
        private Boolean hasSubmission;
        private Boolean isGraded;
        private Integer receivedScores;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentsScoreTable{
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
    public static class WarningInfoList{
        private List<WarningInfo> content;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WarningInfo {
        private WarningType warningType;
        private Long warningStudent;
        private WarningInfoContent description;
        private OffsetDateTime date;
        @Getter
        @AllArgsConstructor
        public enum WarningType{
            homework_uncompleted(AssignmentContent.class),
            low_score(AssignmentContent.class);
            private final Class<?> clazz;
        }
    }

    public interface WarningInfoContent{
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignmentContent implements WarningInfoContent{
        private Assignment assignmentEntity;
    }
}
