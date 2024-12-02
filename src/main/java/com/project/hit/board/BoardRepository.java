package com.project.hit.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findTop6ByTypeContainingOrderByNoDesc(String type);

    Board findById(long id);

//    Board findTopByIdLessThanOrderByIdDesc(Long no);
//
//    Board findTopByIdGreaterThanOrderByIdAsc(Long no);


    Board findTopByNoLessThanOrderByNoDesc(Long currentBoardNo);

    Board findTopByNoGreaterThanOrderByNoAsc(Long currentBoardNo);
}
