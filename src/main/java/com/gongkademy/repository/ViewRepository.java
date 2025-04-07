package com.gongkademy.repository;

import com.gongkademy.domain.View;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewRepository extends JpaRepository<View,Long> {

     Long countByBoard_Id(Long boardId);
}
