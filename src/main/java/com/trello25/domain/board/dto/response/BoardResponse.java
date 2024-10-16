package com.trello25.domain.board.dto.response;

import com.trello25.domain.board.enums.BackColors;
import com.trello25.domain.kanban.dto.response.KanbanResponse;
import java.util.List;

public class BoardResponse {

    private Long id;
    private Long workspaceId;
    private String title;
    private BackColors backColor;
    private List<KanbanResponse> kanbanResponses;

    public BoardResponse(Long id, Long workspaceId, String title, BackColors backColor){
        this.id = id;
        this.workspaceId = workspaceId;
        this.title = title;
        this.backColor = backColor;
    }

    public BoardResponse(Long id, String title, BackColors backColor, List<KanbanResponse> kanbanResponses){
        this.id = id;
        this.title = title;
        this.backColor = backColor;
        this.kanbanResponses = kanbanResponses;
    }
}
