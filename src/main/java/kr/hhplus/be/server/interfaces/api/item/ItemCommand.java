package kr.hhplus.be.server.interfaces.api.item;

import kr.hhplus.be.server.domain.item.Item;

public class ItemCommand {

    public static class Response {

        public Item item;

        public Item getItem() {
            return item;
        }

        public Response(Item item) {
            this.item = item;
        }


    }
}
