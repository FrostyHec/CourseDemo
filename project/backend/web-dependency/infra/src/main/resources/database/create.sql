-- write the DDL at here
-- please ensure immutability ( drop if exists then create )
-- 创建数据库 CourseDemo
-- CREATE DATABASE CourseDemo;

-- 删除所有表格（如果存在）
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS chapters;
DROP TABLE IF EXISTS materials;
DROP TABLE IF EXISTS assignments;
DROP TABLE IF EXISTS submissions;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS course_likes;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS comments;

-- 创建用户表（Users）
CREATE TABLE users
(
    user_id    BIGSERIAL PRIMARY KEY,                             -- 自增用户ID
    username   VARCHAR(50) NOT NULL,                           -- 用户名
--     email      VARCHAR(100) NOT NULL UNIQUE,                   -- 邮箱，必须唯一
    password   VARCHAR(255) NOT NULL,                          -- 密码
    role       VARCHAR(20) CHECK (role IN ('admin', 'teacher', 'student')) NOT NULL,  -- 用户角色，限定为'admin'，'teacher'或'student'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP             -- 用户创建时间
);

-- 创建课程表（Courses）
CREATE TABLE courses
(
    course_id   BIGSERIAL PRIMARY KEY,                                                  -- 自增课程ID
    course_name VARCHAR(100) NOT NULL,                                               -- 课程名称
    description TEXT,                                                                -- 课程描述
    teacher_id  INT NOT NULL,                                                        -- 教师ID
    status      VARCHAR(20) CHECK (status IN ('created', 'submitted', 'approved', 'rejected', 'archived')) NOT NULL, -- 课程状态
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                                 -- 课程创建时间
    visibility  VARCHAR(20) CHECK (status IN ('open', 'closed', 'semi-open')) NOT NULL  -- 课程可见性
    -- FOREIGN KEY (teacher_id) REFERENCES Users (user_id) ON DELETE CASCADE         -- 教师ID外键，已注释
);

-- 创建章节表（Chapters）
CREATE TABLE chapters
(
    chapter_id    BIGSERIAL PRIMARY KEY,                                               -- 自增章节ID
    course_id     INT NOT NULL,                                                     -- 课程ID
    chapter_title VARCHAR(100) NOT NULL,                                            -- 章节标题
    chapter_type  VARCHAR(20) CHECK (chapter_type IN ('teaching', 'assignment', 'project')) NOT NULL, -- 章节类型
    content       TEXT,                                                             -- 章节内容
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP                               -- 章节创建时间
    -- FOREIGN KEY (course_id) REFERENCES Courses (course_id) ON DELETE CASCADE     -- 课程ID外键，已注释
);

-- 创建课件表（Materials）
CREATE TABLE materials
(
    material_id     BIGSERIAL PRIMARY KEY,                                             -- 自增课件ID
    chapter_id      INT NOT NULL,                                                   -- 章节ID
    file_path       VARCHAR(255) NOT NULL,                                          -- 文件路径
    file_type       VARCHAR(10) CHECK (file_type IN ('pdf', 'md')) NOT NULL,        -- 文件类型，限定为'pdf'或'md'
    version         INT DEFAULT 1,                                                  -- 课件版本，默认1
    upload_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                            -- 上传时间
    is_downloadable BOOLEAN DEFAULT TRUE                                            -- 是否可下载
    -- FOREIGN KEY (chapter_id) REFERENCES Chapters (chapter_id) ON DELETE CASCADE  -- 章节ID外键，已注释
);

-- 创建作业表（Assignments）
CREATE TABLE assignments
(
    assignment_id          BIGSERIAL PRIMARY KEY,                                      -- 自增作业ID
    chapter_id             INT NOT NULL,                                            -- 章节ID
    assignment_description TEXT NOT NULL,                                           -- 作业描述
    due_date               DATE NOT NULL                                            -- 作业截止日期
    -- FOREIGN KEY (chapter_id) REFERENCES Chapters (chapter_id) ON DELETE CASCADE  -- 章节ID外键，已注释
);

-- 创建提交作业表（Submissions）
CREATE TABLE submissions
(
    submission_id   BIGSERIAL PRIMARY KEY,                                             -- 自增提交ID
    assignment_id   INT NOT NULL,                                                   -- 作业ID
    student_id      INT NOT NULL,                                                   -- 学生ID
    file_path       VARCHAR(255) NOT NULL,                                          -- 提交文件路径
    submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                            -- 提交时间
    grade           DECIMAL(3, 2),                                                  -- 成绩
    feedback        TEXT                                                            -- 反馈
    -- FOREIGN KEY (assignment_id) REFERENCES Assignments (assignment_id) ON DELETE CASCADE  -- 作业ID外键，已注释
    -- FOREIGN KEY (student_id) REFERENCES Users (user_id) ON DELETE CASCADE         -- 学生ID外键，已注释
);

-- 创建评论表（Comments）
CREATE TABLE comments
(
    comment_id   BIGSERIAL PRIMARY KEY,                                                -- 自增评论ID
    chapter_id   INT NOT NULL,                                                      -- 章节ID
    user_id      INT NOT NULL,                                                      -- 用户ID
    comment_text TEXT NOT NULL,                                                     -- 评论内容
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP                                -- 评论创建时间
    -- FOREIGN KEY (chapter_id) REFERENCES Chapters (chapter_id) ON DELETE CASCADE  -- 章节ID外键，已注释
    -- FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE           -- 用户ID外键，已注释
);

-- 创建通知表（Notifications）
CREATE TABLE notifications
(
    notification_id BIGSERIAL PRIMARY KEY,                                             -- 自增通知ID
    course_id       INT NOT NULL,                                                   -- 课程ID
    sender_id       INT NOT NULL,                                                   -- 发送者ID
    receiver_id     INT NOT NULL,                                                   -- 接收者ID
    message         TEXT NOT NULL,                                                  -- 通知内容
    is_read         BOOLEAN DEFAULT FALSE,                                          -- 是否已读
    sent_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             -- 发送时间
    -- FOREIGN KEY (course_id) REFERENCES Courses (course_id) ON DELETE CASCADE     -- 课程ID外键，已注释
    -- FOREIGN KEY (sender_id) REFERENCES Users (user_id) ON DELETE CASCADE         -- 发送者ID外键，已注释
    -- FOREIGN KEY (receiver_id) REFERENCES Users (user_id) ON DELETE CASCADE       -- 接收者ID外键，已注释
);

-- 创建课程点赞表（CourseLikes）
CREATE TABLE course_likes
(
    like_id    BIGSERIAL PRIMARY KEY,                                                  -- 自增点赞ID
    course_id  INT NOT NULL,                                                        -- 课程ID
    student_id INT NOT NULL                                                         -- 学生ID
    -- FOREIGN KEY (course_id) REFERENCES Courses (course_id) ON DELETE CASCADE     -- 课程ID外键，已注释
    -- FOREIGN KEY (student_id) REFERENCES Users (user_id) ON DELETE CASCADE        -- 学生ID外键，已注释
);

-- 创建课程注册表（Enrollments）
CREATE TABLE enrollments
(
    enrollment_id   BIGSERIAL PRIMARY KEY,                                             -- 自增注册ID
    student_id      INT NOT NULL,                                                   -- 学生ID
    course_id       INT NOT NULL,                                                   -- 课程ID
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             -- 注册时间
    -- FOREIGN KEY (student_id) REFERENCES Users (user_id) ON DELETE CASCADE        -- 学生ID外键，已注释
    -- FOREIGN KEY (course_id) REFERENCES Courses (course_id) ON DELETE CASCADE     -- 课程ID外键，已注释
);
