package com.project.hit.board;

import com.project.hit.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Board getBoardById(Long id) {
        return this.boardRepository.findById(id).orElse(null);
    }

    public Board getBoard(Long id) {
        Optional<Board> board = this.boardRepository.findById(id);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new DataNotFoundException("board not found");
        }
    }


    public Board getPreviousBoard(Long currentBoardNo, String type) {
        return boardRepository.findTopByTypeAndNoLessThanOrderByNoDesc(type, currentBoardNo);
    }

    public Board getNextBoard(Long currentBoardNo, String type) {
        return boardRepository.findTopByTypeAndNoGreaterThanOrderByNoAsc(type, currentBoardNo);
    }
}
