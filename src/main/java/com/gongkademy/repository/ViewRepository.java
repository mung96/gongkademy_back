package com.gongkademy.repository;

import com.gongkademy.domain.View;
import java.util.List;

public interface ViewRepository {

     Long save(View view);
     List<View> findAllByBoardId(Long boardId);
}
