package com.tansen.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment extends AbstractEntity {
    @Column(name = "message", nullable = false, length = 200)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_by", nullable = false, updatable = false,referencedColumnName = "id")
    private User commentBy;

    @Column(name = "comment_at")
    private LocalDateTime commentAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name =  "unqiue_id")
    private String uniqueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @Column(name = "created_ip")
    private String createdIp;

    @Column(name = "is_delete",  nullable = false )
    private Boolean isDelete;

}
