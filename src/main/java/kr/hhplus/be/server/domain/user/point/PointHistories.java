package kr.hhplus.be.server.domain.user.point;

import java.util.List;

public class PointHistories {

    private List<PointHistory> pointHistories;

    public void addPointHistory(PointHistory pointHistory) {
        pointHistories.add(pointHistory);
    }

}
