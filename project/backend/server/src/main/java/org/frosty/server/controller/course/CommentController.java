package org.frosty.server.controller.course;

import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.services.course.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathConstant.API)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 对课程发布评论
    @PostMapping("/resource/{id}/comment")
    public void addCommentToResource(
            @GetToken TokenInfo tokenInfo,
            @PathVariable Long id,
            @RequestBody ResourceComment comment) {
        commentService.addCommentToResource(id, comment);
//        return Response.getSuccess("Comment added to resource successfully.");
    }

    // 对评论发布评论（回复）
    @PostMapping("/resource/comment/{id}/comment")
    public void addReplyToComment(
            @GetToken TokenInfo tokenInfo,
            @PathVariable Long id,
            @RequestBody ResourceComment reply) {
        commentService.addReplyToComment(id, reply);
//        return Response.getSuccess("Reply added to comment successfully.");
    }

    // 修改评论
    @PutMapping("/resource/comment/{id}")
    public void updateComment(@GetToken TokenInfo tokenInfo,@PathVariable Long id, @RequestBody ResourceComment updatedComment) {
        updatedComment.setCommentId(id);
        commentService.updateComment(id, updatedComment);
//        return Response.getSuccess("Comment updated successfully.");
    }

    // 删除评论
    @DeleteMapping("/resource/comment/{id}")
    public void deleteComment(@GetToken TokenInfo tokenInfo,@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    // 获取评论
    @GetMapping("/resource/comment/{id}")
    public Response getComment(@GetToken TokenInfo tokenInfo,@PathVariable Long id) {
        ResourceComment comment = commentService.findById(id);
        return Response.getSuccess(comment);
    }

    // 获取全部评论
    @GetMapping("/resource/{id}/comments")
    public Response getAllComments(@GetToken TokenInfo tokenInfo,@PathVariable Long id) {
        List<ResourceComment> comments = commentService.findAllByResourceId(id);
        return Response.getSuccess(comments);
    }
}
