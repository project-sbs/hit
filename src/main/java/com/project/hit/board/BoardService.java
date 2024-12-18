package com.project.hit.board;

import com.project.hit.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public void insertBoard(Board board) {
        boardRepository.save(board);
    }

    public List<Board> getTop6Boards(String type) {
        return this.boardRepository.findTop6ByTypeEqualsOrderByNoDesc(type);
    }

    public List<Board> getTop3Schedulers(String type) {
        List<Board> schedulers = boardRepository.findTop6ByTypeEqualsOrderByNoDesc(type);
        return schedulers.size() > 3 ? schedulers.subList(0, 3) : schedulers;
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

    public List<Board> getSchedulersByPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return boardRepository.findByTypeOrderByDateDesc("scheduler", pageRequest).getContent();
    }

    public int getTotalSchedulersCount() {
        return boardRepository.countByType("scheduler");
    }

}
