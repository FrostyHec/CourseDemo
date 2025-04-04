<div align=center>

# CourseDemo : CS309 Project

[中文](#项目介绍) /
[English](#project-introduction)

南方科技大学2024秋季 `CS309 面向对象程序设计与分析`期末大作业：**CourseDemo在线学习平台**

Southern University of Science and Technology, Autumn 2024
`CS309 Object-oriented Analysis and Design` Bonus Project: **CourseDemo-Online learning platform**

基于微服务架构from scratch实现的在线学习平台，主要使用SpringBoot3 + Vue3进行开发，包含课程信息与资源管理、课程讨论、作业提交、消息提醒、学习进度与成绩分析、
教学视频播放、在线直播课程、LLM对话等功能

An online learning platform implemented from scratch based on a microservices architecture, primarily developed using
Spring Boot 3 and Vue 3. It includes features such as course information and resource management, course discussions,
assignment submissions, message notifications, learning progress and performance analysis, video playback, online live
courses, and LLM discussion.

开发列表 / Developers : [@Frosky Lrupotkin](https://github.com/FrostyHec) | [@Maples0127](https://github.com/Maples0127)
| [@DrTeresia](https://github.com/DrTeresia) | [@Nyanpassing](https://github.com/Nyanpassing) | [@H-112](https://github.com/H-112)

(排名不分先后 / Name Order is Random)

得分 / Score : 97/100

![example-fig.jpg](docs%2Fexample-fig.jpg)

</div>

[英语翻译]

# Project Introduction

### Project Structure

```
CourseDemo
├── project             
│   ├── backend                  # Backend code
│   ├── deployment               # Deployment code
|   └── frontend                 # Frontend code
├── docs 
│   ├── requirement              # Requirement documentation
│   ├── Final-Pre                # Final project report
│   └── Midterm-pre              # Midterm project report                 
├── .gitignore
├── LICENSE
└── README.md
```

### Tasks

The platform implements the following features:

- [x] Course Metadata Management
    - [x] Role-based account management (Student, Teacher, Administrator)
    - [x] Course creation requests
    - [x] Course review
- [x] Course Personnel Management
    - [x] Set course accessibility (Open enrollment / Invitation-based / Semi-open courses)
    - [x] Implementation of invitation-based / application-based course enrollment
- [x] Course Resource Management
    - [x] Upload course resources
    - [x] Resource version control
    - [x] Online resource viewing (PDF, markdown, videos, etc.)
    - [x] Granular access control
- [x] Course Notification Management
    - [x] Course announcements
    - [x] Real-time in-platform messages
    - [x] Email notifications
- [x] Student Discussions
    - [x] Resource discussion boards
    - [x] Upload images/resources in discussion areas
- [x] Course Evaluation
    - [x] Public course recommendations
    - [x] Popular course rankings
    - [x] Course ratings and reviews
    - [x] Teaching evaluation surveys
- [x] Assignments
    - [x] Assignment distribution and collection
    - [x] Assignment grading
    - [x] Assignment submission reminders
- [x] Learning Progress Management
    - [x] Resource browsing progress
        - [x] Video watching progress
        - [x] Anti-cheating features for videos
            - [x] Enforce minimum playback duration
            - [x] Block simultaneous playback
            - [x] Idle detection
            - [x] Anti-cheating script defense
        - [ ] Video comments (bullet screen)
- [x] Performance Analysis
    - [x] Grade statistics
    - [x] Assignment completion statistics
    - [x] Course learning progress management
    - [x] Student learning warnings
- [x] Course Live Streaming
    - [x] Live Streaming Channel
    - [x] Live room management
    - [x] Real-time comments (dammu)
- [x] Points Mall
    - [x] User behavior rewards
    - [x] Points statistics
    - [x] Redeem virtual products with points
- [x] Single-device login restriction
- [x] Real-time Knowledge Q&A with Large Language Models
    - [x] Real-time interaction
    - [x] Chat summarization
    - [x] Conversation session management
- [x] Multi-platform support
    - [x] Android APK packaging
    - [ ] Mini-programs
- [ ] Online Deployment
    - [x] Docker packaging
    - [ ] Cluster deployment
- [x] Testing
    - [x] API testing
    - [x] Frontend logic testing
    - [ ] E2E GUI testing
    - [x] Integration environment testing

## Development Summary

Duration: Approximately 8 weeks

### Technical Summary

Thankfully the project is finally complete (So many requirements for this project XD). Coupled with other
projects, finishing this one felt quite exhausting. It seems that challenges in project management can sometimes
outweigh technical challenges and there’s still much to learn XD.

#### Project Management:

1. Comprehensive testing is crucial for maintaining project quality and reducing difficulties in frontend-backend
   integration.
2. Establishing norms for code workflows, development processes, and design standards is quite important. There’s a kind
   of ~~frustrating but~~ satisfying orderliness to it.
3. API documentation and version control are critical, and there are plenty of tools for these tasks. Personally, I
   prefer management tools designed around the "code-as-documentation" philosophy XD.
4. Non-technical aspects like project information management and requirement management (which CS courses don’t teach)
   are surprisingly important 😂. Good project and requirement management can maximize development efficiency. (Perhaps
   this is due to the vague official requirements that forced me to also act as a product manager (x).)

#### Design Paradigms: Code quality is still essential.

1. The design patterns taught in OOAD are still quite important (though I’ve known many of them for a long time XD).
   Among these, the registry pattern, singleton pattern, and dependency injection (DI) pattern are particularly crucial.
   They are soul-level paradigms for leveraging OO polymorphism and achieving project decoupling (very helpful for
   avoiding messy logic 🤬).
2. In addition to discussing design patterns, the SOLID principles are also quite important (surprisingly, these five
   principles weren’t covered in class O.O).
3. Function decoupling and extensibility are key to project quality. However, overall design should involve trade-offs
   based on requirements, maintaining decoupling while avoiding overdesign.
4. Java 8 and later syntax features are quite fascinating. Stream-based and annotation-based features make coding truly
   enjoyable and reading code genuinely elegant.
5. Designing authentication logic (basic authentication, API access control, resource access control) and anti-cheating
   mechanisms requires thoughtful consideration. I plan to organize my design ideas later.

### Final Thoughts

A big thank you to the team members for their hard work over the past two months. Despite the late nights debugging, the
experience was ultimately rewarding.

I’m also very grateful for the OOAD course, which provided this learning opportunity and valuable experiences related to
design paradigms and project management.

— Forester

## 项目介绍

### 项目结构

```
CourseDemo
├── project             
│   ├── backend                  # 后端代码
│   ├── deployment               # 部署代码
|   └── frontend                 # 前端代码
├── docs 
│   ├── requirement              # 需求文档
│   ├── Final-Pre                # 结束项目报告
│   └── Midterm-pre              # 中期项目报告                 
├── .gitignore
├── LICENSE
└── README.md
```

### 工作任务

本平台实现了以下功能：

- [x] 课程元信息管理
    - [x] 账户角色划分（学生、教师、管理员）
    - [x] 课程创建申请
    - [x] 课程审核
- [x] 课程人员管理
    - [x] 设置课程开放程度（开放加入 / 邀请制 / 半开放课程）
    - [x] 邀请制 / 申请制入课实现
    - [x] 
- [x] 课程资源管理
    - [x] 课程资源上传
    - [x] 资源版本管理
    - [x] 资源在线查看（pdf,md,视频等）
    - [x] 开放粒度设置
- [x] 课程通知管理
    - [x] 课程公告
    - [x] 实时站内消息信
    - [x] 邮件通知
- [x] 学生讨论
    - [x] 资源讨论版
    - [x] 讨论区上传图片 / 资源
- [x] 课程评价
    - [x] 公开课程推荐
    - [x] 热门课程排名
    - [x] 课程评分与评论
    - [x] 教学评价问卷
- [x] 作业
    - [x] 作业发布与回收
    - [x] 作业批改
    - [x] 作业提交提醒
- [x] 学习进度管理
    - [x] 资源浏览进度
        - [x] 视频观看进度
        - [x] 视频防作弊功能
            - [x] 强制播放时长
            - [x] 阻塞同时播放
            - [x] 挂机检查
            - [x] 作弊脚本防御
        - [ ] 视频弹幕
- [x] 成绩分析
    - [x] 成绩统计
    - [x] 作业完成情况统计
    - [x] 课程学习进度管理
    - [x] 学生学习预警
- [x] 课程直播
    - [x] 实时直播
    - [x] 直播间管理
    - [x] 实时弹幕
- [x] 积分商城
    - [x] 用户行为奖励
    - [x] 积分统计
    - [x] 虚拟产品积分兑换
- [x] 单端登录限制
- [x] 大模型实时知识问答
    - [x] 实时交互
    - [x] 聊天总结
    - [x] 对话session管理
- [x] 多平台
    - [x] Android APK打包
    - [ ] 小程序
- [ ] 线上部署
    - [x] docker打包
    - [ ] 集群部署
- [x] 测试
    - [x] API测试
    - [x] 前端逻辑测试
    - [ ] E2E GUI测试
    - [x] 集成环境测试

## 开发总结

工期：约8周

### 技术总结

怎么说也算是肝完了，没想到这个project需求这么多，配合着一堆其它project做完真感觉有点憔悴，感觉项目管理方面的challenge会
比单纯的技术challenge还高一点，看来还是要多学习XD

项目管理：

1. 充分的测试是维护工程质量与降低前后端对接难度的重要手段
2. 代码流程、开发流程规范与设计规范的制定还蛮重要的，有一种~~反人类但是~~井井有条的美
3. API doc与版本管理相当重要&也有相当多的工具，不过还是最喜欢基于代码即文档思路设计的管理工具XD
4. 项目信息管理&需求管理这种non-tech的、cs专业不教的东西其实反而相当重要😂，好的项目与需求管理才能最大化开发效率，
   当然这有可能是因为模糊的official requirement使得需要同时兼任产品经理的工作（x）

设计范式：代码质量还是很重要滴

1. 感觉OOAD所教的设计模式还是很重要的（虽然很多很早就知道了XD），在设计模式里感觉注册器模式、单例模式和
   DI模式蛮重要的，属于是灵魂级使用OO多态&项目解耦的重要范式（十分有助于避免生成一坨的logic🤬）
2. 除了discuss设计模式外SOLID五原则也是蛮重要的（这个五原则课上居然不讲咩O.O）
3. 功能的解耦&可扩展性是项目质量的关键，不过整体上还是要依据需求有trade off，保持解耦的同时避免过度设计
4. Java8以后的许多语法特性还算fascinate，流式&注解的特性只能说让写起来是真爽&读起来是真优雅
5. 鉴权逻辑(基本鉴权，API访问鉴权，资源访问鉴权）和防作弊机制的设计还是要下点功夫思考的（尤其是鉴权的实现），后续可以整理一下设计思路

### 后记

十分感谢小组几位同学在两个月的时间的辛苦付出啦，一起调bug的夜晚虽然辛苦，但终归是有收获的。

也十分感谢OOAD课程所提供的这次学习机会，收获了很多与设计范式&项目管理相关的经验和体会。

— Forester
