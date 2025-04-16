package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.point.pointHistory.PointHistory;

import java.util.List;

public class PointHistories {

    private List<PointHistory> pointHistories;

    public void registerPointHistory(PointHistory pointHistory) {
        pointHistories.add(pointHistory);
    }

}
