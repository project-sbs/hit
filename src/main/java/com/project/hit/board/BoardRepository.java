package com.project.hit.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findTop6ByTypeEqualsOrderByNoDesc(String type);

    Board findTopByTypeAndNoLessThanOrderByNoDesc(String type, Long no);

    Board findTopByTypeAndNoGreaterThanOrderByNoAsc(String type, Long no);

    Page<Board> findByType(String type, Pageable pageable);

    Page<Board> findByDateAndTypeOrderByNoDesc(String date, String type, Pageable pageable);

    Page<Board> findByTypeOrderByDateDesc(String type, Pageable pageable);

    int countByType(String scheduler);
}
