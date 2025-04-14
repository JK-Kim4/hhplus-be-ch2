package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.point.pointHistory.PointHistoryV2;

import java.util.List;

public class PointHistories {

    private List<PointHistoryV2> pointHistories;

    public void registerPointHistory(PointHistoryV2 pointHistory) {
        pointHistories.add(pointHistory);
    }

}
