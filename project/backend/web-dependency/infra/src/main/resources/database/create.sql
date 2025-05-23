-- 初始trigger
CREATE OR REPLACE FUNCTION auto_time()
    RETURNS TRIGGER AS
$$
BEGIN
    IF
        TG_OP = 'INSERT' THEN
        NEW.created_at = now();
        NEW.updated_at
            = now();
    ELSIF
        TG_OP = 'UPDATE' THEN
        NEW.updated_at = now();
    END IF;
    RETURN NEW;
END;
$$
    LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION auto_time_only_created()
    RETURNS TRIGGER AS
$$
BEGIN
    IF TG_OP = 'INSERT' THEN
        NEW.created_at = now();
    END IF;
    RETURN NEW;
END;
$$
    LANGUAGE 'plpgsql';


-- 创建用户表（Users）
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    user_id    BIGSERIAL PRIMARY KEY,                                                       -- 自增用户ID
    first_name VARCHAR(50)                                                 NOT NULL,        -- 用户名
    last_name  VARCHAR,
    password   VARCHAR(255)                                                NOT NULL,        -- 密码
    role       VARCHAR(20) CHECK (role IN ('admin', 'teacher', 'student')) NOT NULL,        -- 用户角色，限定为'admin'，'teacher'或'student'
    email      VARCHAR                                                     NOT NULL UNIQUE, -- 邮箱，必须唯一
    created_at TIMESTAMP WITH TIME ZONE                                    NOT NULL,        -- 用户创建时间
    updated_at TIMESTAMP WITH TIME ZONE                                    NOT NULL         -- 用户更新时间
);
CREATE
    OR REPLACE TRIGGER auto_users_time
    BEFORE INSERT OR
        UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE auto_time();


-- 创建课程表（Courses）
DROP TABLE IF EXISTS courses;
CREATE TABLE courses
(
    course_id       BIGSERIAL PRIMARY KEY,                                                                                 -- 自增课程ID
    course_name     VARCHAR(100)                                                                                 NOT NULL, -- 课程名称
    description     TEXT,                                                                                                  -- 课程描述
    teacher_id      BIGINT                                                                                       NOT NULL, -- 教师ID
    status          VARCHAR(20) CHECK (status IN ('creating', 'submitted', 'published', 'rejected', 'archived')) NOT NULL, -- 课程状态
    created_at      TIMESTAMP WITH TIME ZONE                                                                     NOT NULL, -- 课程创建时间
    updated_at      TIMESTAMP WITH TIME ZONE                                                                     NOT NULL,
    publication     VARCHAR(20) CHECK (publication IN ('open', 'closed', 'semi_open'))                           NOT NULL,
    evaluation_type VARCHAR                                                                                      NOT NULL  --如果设置为0则无教评版本
    -- 课程可见性
    -- FOREIGN KEY (teacher_id) REFERENCES Users (user_id) ON DELETE CASCADE         -- 教师ID外键，已注释
);
CREATE
    OR REPLACE TRIGGER auto_courses_time
    BEFORE INSERT OR
        UPDATE
    ON courses
    FOR EACH ROW
EXECUTE PROCEDURE auto_time();


-- 创建章节表（Chapters）
DROP TABLE IF EXISTS chapters;
CREATE TABLE chapters
(
    chapter_id    BIGSERIAL PRIMARY KEY,                                                              -- 自增章节ID
    course_id     BIGINT                                                                    NOT NULL, -- 课程ID
    chapter_title VARCHAR(100)                                                              NOT NULL, -- 章节标题
    chapter_type  VARCHAR(20) CHECK (chapter_type IN ('teaching', 'assignment', 'project')) NOT NULL, -- 章节类型
    content       TEXT,                                                                               -- 章节内容
    chapter_order INT                                                                       NOT NULL, -- 章节顺序，小的前端显示在上
    visible       BOOLEAN                                                                   NOT NULL, -- 和student有关
    publication   BOOLEAN                                                                   NOT NULL, -- 也和student有关
    created_at    TIMESTAMP WITH TIME ZONE                                                  NOT NULL, -- 章节创建时间
    updated_at    TIMESTAMP WITH TIME ZONE                                                  NOT NULL
    -- FOREIGN KEY (course_id) REFERENCES Courses (course_id) ON DELETE CASCADE     -- 课程ID外键，已注释
);
CREATE
    OR REPLACE TRIGGER auto_chapters_time
    BEFORE INSERT OR
        UPDATE
    ON chapters
    FOR EACH ROW
EXECUTE PROCEDURE auto_time();

-- -- 创建课程注册表（Enrollments）
DROP TABLE IF EXISTS enrollments;
CREATE TABLE enrollments
(
    student_id INT                                               NOT NULL, -- 学生ID
    course_id  INT                                               NOT NULL, -- 课程ID
    status     VARCHAR CHECK ( status IN ('publik', 'invited') ) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE                          NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE                          NOT NULL,
    primary key (student_id, course_id)
    -- FOREIGN KEY (student_id) REFERENCES Users (user_id) ON DELETE CASCADE        -- 学生ID外键，已注释
    -- FOREIGN KEY (course_id) REFERENCES Courses (course_id) ON DELETE CASCADE     -- 课程ID外键，已注释
);
CREATE
    OR REPLACE TRIGGER auto_enrollments_time
    BEFORE INSERT OR
        UPDATE
    ON enrollments
    FOR EACH ROW
EXECUTE PROCEDURE auto_time();

-- 创建课程资源表（Resources）
DROP TABLE IF EXISTS resources;
CREATE TABLE resources
(
    resource_id            BIGSERIAL PRIMARY KEY,             -- 自增课件ID
    chapter_id             INT                      NOT NULL, -- 章节ID
    resource_name          VARCHAR                  NOT NULL,
    suffix                 VARCHAR                  NOT NULL,-- 文件类型，限定为'pdf'或'md'
    file_name              VARCHAR                  NOT NULL, -- UUID4+RESOURCE-NAME
    resource_order         INT                      NOT NULL,
    resource_version_name  VARCHAR                  NOT NULL,
    resource_version_order INT                      NOT NULL,
    resource_type          VARCHAR                  NOT NULL CHECK ( resource_type IN ('description', 'courseware', 'video', 'attachment') ),
    student_can_download   BOOLEAN                  NOT NULL, -- 是否可下载
    created_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at             TIMESTAMP WITH TIME ZONE NOT NULL
    -- FOREIGN KEY (chapter_id) REFERENCES Chapters (chapter_id) ON DELETE CASCADE  -- 章节ID外键，已注释
);
CREATE
    OR REPLACE TRIGGER auto_resources_time
    BEFORE INSERT OR
        UPDATE
    ON resources
    FOR EACH ROW
EXECUTE PROCEDURE auto_time();

-- 创建通知表（Notifications）
DROP TABLE IF EXISTS notifications;
CREATE TABLE notifications
(
    notification_id BIGSERIAL PRIMARY KEY,               -- 自增通知ID
    course_id       INT  NOT NULL,                       -- 课程ID
    title           TEXT NOT NULL,                       -- 通知标题
    message         TEXT NOT NULL,                       -- 通知内容
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- 更新时间
    -- FOREIGN KEY (course_id) REFERENCES Courses (course_id) ON DELETE CASCADE     -- 课程ID外键，已注释
    -- FOREIGN KEY (sender_id) REFERENCES Users (user_id) ON DELETE CASCADE         -- 发送者ID外键，已注释
    -- FOREIGN KEY (receiver_id) REFERENCES Users (user_id) ON DELETE CASCADE       -- 接收者ID外键，已注释
);
CREATE
    OR REPLACE TRIGGER auto_notification_time
    BEFORE INSERT OR
        UPDATE
    ON notifications
    FOR EACH ROW
EXECUTE PROCEDURE auto_time();


DROP TABLE IF EXISTS notification_receiver;
CREATE TABLE notification_receiver
(
    notification_id BIGINT NOT NULL,
    receiver_id     BIGINT NOT NULL,
    primary key (notification_id, receiver_id)
--     foreign key (notification_id) references notifications(notification_id) on delete cascade,
--     foreign key (receiver_id) references users(user_id) on delete cascade
);
-- 创建课程点赞表（CourseLikes）
DROP TABLE IF EXISTS course_likes;
CREATE TABLE course_likes
(
    course_id  INT NOT NULL, -- 课程ID
    student_id INT NOT NULL, -- 学生ID
    primary key (course_id, student_id)
);

-- 创建评论表（Comments）
DROP TABLE IF EXISTS resource_comments;
CREATE TABLE resource_comments
(
    comment_id    BIGSERIAL PRIMARY KEY,             -- 自增评论ID
    resource_id   INT                      NOT NULL, -- 章节ID
    user_id       INT                      NOT NULL, -- 用户ID
    comment_text  TEXT                     NOT NULL, -- 评论内容
    comment_reply BIGINT,                            -- 回复评论ID
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL
    -- FOREIGN KEY (chapter_id) REFERENCES Chapters (chapter_id) ON DELETE CASCADE  -- 章节ID外键，已注释
    -- FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE           -- 用户ID外键，已注释
);
CREATE
    OR REPLACE TRIGGER auto_resource_comment_time
    BEFORE INSERT OR
        UPDATE
    ON resource_comments
    FOR EACH ROW
EXECUTE PROCEDURE auto_time();
--课程评论的文件资源
DROP TABLE IF EXISTS comment_resources;
CREATE TABLE comment_resources
(
    id            BIGSERIAL PRIMARY KEY,
    comment_id    BIGINT  NOT NULL, -- 自增评论ID
    resource_name VARCHAR NOT NULL,
    file_name     VARCHAR NOT NULL, -- UUID4+RESOURCE-NAME
    suffix        VARCHAR NOT NULL-- 文件类型，限定为'pdf'或'md'
    -- FOREIGN KEY (comment_id) REFERENCES resource_comments (comment_id) ON DELETE CASCADE  -- 章节ID外键，已注释
);

-- 创建课程评价表（CourseEvaluation）
DROP TABLE IF EXISTS course_evaluations;
CREATE TABLE course_evaluations
(
    course_id              BIGSERIAL                NOT NULL,
    student_id             BIGSERIAL                NOT NULL,
    comment                TEXT                     NOT NULL,
    score                  INT, --可暂时不评分
    evaluation_form_answer json,-- 可暂时不填评教
    created_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    primary key (course_id, student_id)
);
CREATE
    OR REPLACE TRIGGER auto_course_evaluation
    BEFORE INSERT OR
        UPDATE
    ON course_evaluations
    FOR EACH ROW
EXECUTE PROCEDURE auto_time();

drop table if exists assignments;
CREATE TABLE assignments
(
    assignment_id               BIGSERIAL PRIMARY KEY,             -- 自增作业ID
    chapter_id                  INT                      NOT NULL, -- 章节ID
    assignment_name             VARCHAR                  NOT NULL,
    description                 TEXT                     NOT NULL, -- 作业描述
    assignment_type             VARCHAR                  NOT NULL CHECK ( assignments.assignment_type IN ('file_upload', 'online_form') ),
    allow_update_submission     BOOLEAN                  NOT NULL,
    latest_submission_time      TIMESTAMP WITH TIME ZONE NOT NULL,
    maximum_score               INT                      NOT NULL,
    allow_student_to_view_score BOOLEAN                  NOT NULL,
    created_at                  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at                  TIMESTAMP WITH TIME ZONE NOT NULL
--     ,FOREIGN KEY (chapter_id) REFERENCES chapters (chapter_id) ON DELETE CASCADE
);
CREATE
    OR REPLACE TRIGGER auto_assignment_time
    BEFORE INSERT OR
        UPDATE
    ON assignments
    FOR EACH ROW
EXECUTE PROCEDURE auto_time();

drop table if exists file_submission;
CREATE TABLE file_submission
(
    file_submission_id BIGSERIAL PRIMARY KEY,
    assignment_id      BIGINT                   NOT NULL,
    student_id         BIGINT                   NOT NULL,
    suffix             VARCHAR                  NOT NULL,
    file_name          VARCHAR                  NOT NULL,
    gained_score       INT,
    created_at         TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at         TIMESTAMP WITH TIME ZONE NOT NULL,
    unique (assignment_id, student_id)
--     ,FOREIGN KEY (assignment_id) REFERENCES assignments (assignment_id) ON DELETE CASCADE
);
CREATE
    OR REPLACE TRIGGER auto_file_submission_time
    BEFORE INSERT OR
        UPDATE
    ON file_submission
    FOR EACH ROW
EXECUTE PROCEDURE auto_time();

DROP TABLE IF EXISTS video_required_seconds;
CREATE TABLE video_required_seconds
(
    video_id         BIGINT NOT NULL,
    required_seconds INT    NOT NULL
--     foreign key (video_id) references resources(resource_id) on delete cascade
);

DROP TABLE IF EXISTS video_watch_records;
CREATE TABLE video_watch_records
(
    video_id                BIGINT NOT NULL,
    student_id              BIGINT NOT NULL,
    remain_required_seconds INT    NOT NULL,
    last_watched_seconds    INT    NOT NULL, -- 这个指的是上次视频停在了哪里，而不是总共看了多少秒
    primary key (video_id, student_id)
--     ,foreign key (video_id) references resources(resource_id) on delete cascade
--     ,foreign key (student_id) references users(user_id) on delete cascade
);

DROP TABLE IF EXISTS course_complete_records;
CREATE TABLE course_complete_records
(
    course_id  BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    primary key (course_id, student_id)
);

DROP TABLE IF EXISTS chapter_complete_records;
CREATE TABLE chapter_complete_records
(
    chapter_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    primary key (chapter_id, student_id)
);

DROP TABLE IF EXISTS resource_complete_records;
CREATE TABLE resource_complete_records
(
    resource_id BIGINT NOT NULL,
    student_id  BIGINT NOT NULL,
    primary key (resource_id, student_id)
);

drop table if exists consume_record;
CREATE TABLE consume_record
(
    record_id     BIGSERIAL PRIMARY KEY,
    user_id       BIGINT                   NOT NULL,
    action_type   VARCHAR                  NOT NULL,
    action_param  JSON                     NOT NULL,
    changed_score INT                      NOT NULL,
    remain_score  INT                      NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE
    OR REPLACE TRIGGER auto_consume_record_time
    BEFORE INSERT OR
        UPDATE
    ON consume_record
    FOR EACH ROW
EXECUTE PROCEDURE auto_time_only_created();

drop table if exists badge_record;
CREATE TABLE badge_record
(
    user_id      BIGINT                   NOT NULL,
    badge_id     BIGINT                   NOT NULL,
    image        VARCHAR                  NOT NULL,
    badge_name   VARCHAR                  NOT NULL,
    market_score INT                      NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    primary key (user_id, badge_id)
);
CREATE
    OR REPLACE TRIGGER auto_badge_record_time
    BEFORE INSERT OR
        UPDATE
    ON badge_record
    FOR EACH ROW
EXECUTE PROCEDURE auto_time_only_created();

drop table if exists market_score_record;
CREATE TABLE market_score_record
(
    user_id      BIGINT NOT NULL,
    market_score INT    NOT NULL,
    primary key (user_id, market_score)
);