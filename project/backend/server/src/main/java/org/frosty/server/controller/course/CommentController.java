package org.frosty.server.controller.course;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.C;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.AuthStatus;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.AuthEx;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.server.entity.bo.CommentResource;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.services.course.CommentService;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 对课程发布评论
    // TODO 2.0版本需改造以支持文件上传
    @PostMapping("/resource/{id}/comment")
    public void addCommentToResource(
//            @GetToken TokenInfo tokenInfo,
            @PathVariable Long id,
            @RequestBody ResourceComment comment) {
        comment.setCommentId(null);
        commentService.addCommentToResource(id, comment);
//        return Response.getSuccess("Comment added to resource successfully.");
    }

    // 对评论发布评论（回复）
    // TODO 2.0版本需改造以支持文件上传
    @PostMapping("/resource/comment/{id}/comment")
    public void addReplyToComment(
//            @GetToken TokenInfo tokenInfo,
            @PathVariable Long id,
            @RequestBody ResourceComment reply) {
        reply.setCommentId(null);
        commentService.addReplyToComment(id, reply);
//        return Response.getSuccess("Reply added to comment successfully.");
    }

    // 修改评论
    @PutMapping("/resource/comment/{id}")
    public void updateComment(
//            @GetToken TokenInfo tokenInfo,
            @PathVariable Long id, @RequestBody ResourceComment updatedComment) {
        updatedComment.setCommentId(id);
        commentService.updateComment(id, updatedComment);
//        return Response.getSuccess("Comment updated successfully.");
    }

    // 删除评论
    @DeleteMapping("/resource/comment/{id}")
    public void deleteComment(
//            @GetToken TokenInfo tokenInfo,
            @PathVariable Long id) {
        commentService.deleteComment(id);// TODO avoid empty foreign key
    }

    // 获取评论
    @GetMapping("/resource/comment/{id}")
    public ResourceComment getComment(
//            @GetToken TokenInfo tokenInfo,
            @PathVariable Long id) {
        return commentService.findById(id);
    }

    // 获取全部评论

    @GetMapping("/resource/{id}/comments")
    public CommentList getAllComments(
            @GetToken TokenInfo tokenInfo,
            @PathVariable Long id) {
        AuthInfo auth = AuthEx.checkPass(tokenInfo);
        List<CommentWithUserAndFileAndAccessKey> comments = commentService.findAllByResourceId(auth,id);
        return new CommentList(comments);
    }

    @PostMapping("/resource/comment/{cid}/file")
    public void uploadFiles(
            @GetToken TokenInfo tokenInfo,
            @PathVariable Long cid,
            @RequestPart("data") CommentResource commentResource,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        AuthEx.checkPass(tokenInfo);
        commentResource.setId(null);
        commentResource.setCommentId(cid);
        commentService.uploadFileForComment(commentResource,file);
    }

    @DeleteMapping("/resource/comment/{cid}/file/{fid}")
    public void removeFiles(
            @PathVariable Long cid, @PathVariable Long fid) {
        commentService.removeFiles(cid, fid);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentWithUserAndFileAndAccessKey {
        @TableId(type = IdType.AUTO)
        private Long commentId;
        private Long resourceId;
        private UserPublicInfo user;
        private String commentText;
        private List<CommentResourceWithAccessKey> commentFiles;
        private Long commentReply;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public CommentWithUserAndFileAndAccessKey(CommentWithUser comment, List<CommentResourceWithAccessKey> resourceWithAccessKeys) {
            this.commentId = comment.getCommentId();
            this.resourceId = comment.getResourceId();
            this.user = comment.getUser();
            this.commentText = comment.getCommentText();
            this.commentReply = comment.getCommentReply();
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
            this.commentFiles = resourceWithAccessKeys;
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentResourceWithAccessKey {
        private CommentResource resourceEntity;
        private String accessKey;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentList {
        List<CommentWithUserAndFileAndAccessKey> content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class CommentWithUser {
        private Long commentId;
        private Long resourceId;
        private UserPublicInfo user;
        private String commentText;
        private Long commentReply;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
    }
}
