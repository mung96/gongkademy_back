package com.gongkademy.repository;

import com.gongkademy.domain.View;
import java.util.List;

public interface ViewRepository {

     void save(View view);
     List<View> findAllByBoardId(Long board_id);
}
