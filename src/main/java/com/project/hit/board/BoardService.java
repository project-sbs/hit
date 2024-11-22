package com.project.hit.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public void insertBoard(Board board) {
        boardRepository.save(board);
    }

    public List<Board> getTop6Boards(String type) {
        return this.boardRepository.findTop6ByTypeContainingOrderByNoDesc(type);
    }
}
