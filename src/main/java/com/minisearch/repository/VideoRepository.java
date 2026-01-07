package com.minisearch.repository;

import com.minisearch.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// VideoRepository knows which table to query because it is typed with the Video entity
// the Video entity is mapped to the videos table via @Entity + @Table in model file.
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query(value = """
        SELECT *
        FROM videos
        WHERE search_vector @@ plainto_tsquery(:keyword)
        ORDER BY ts_rank(search_vector, plainto_tsquery(:keyword)) DESC
        """, nativeQuery = true)

    // @Param("keyword") links the method argument keyword to :keyword in the query
    // Spring/Hibernate automatically converts the JPQL result to Video objects
    List<Video> searchIndexed(@Param("keyword") String keyword);

    @Query("""
        SELECT DISTINCT v
        FROM Video v
        LEFT JOIN v.tags t
        WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(v.description) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(t) LIKE LOWER(CONCAT('%', :q, '%'))
    """)

    // @Param("q") links the method argument keyword to :q in the query
    // Spring/Hibernate automatically converts the JPQL result to Video objects
    List<Video> searchBasic(@Param("q") String keyword);
}
