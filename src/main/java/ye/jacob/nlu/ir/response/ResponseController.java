package ye.jacob.nlu.ir.response;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class ResponseController {

    private final String INDEX = "ir";
    private final String TYPE = "qa";

    @Autowired
    private TransportClient client;

    @GetMapping("/get/ir/qa")
    @ResponseBody
    public ResponseEntity getById(@RequestParam(name = "id", defaultValue = "") String id) {
        if (id.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        GetResponse result = this.client.prepareGet(INDEX, TYPE, id).get();

        if (!result.isExists()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(result.getSource(), HttpStatus.OK);
    }

    @PostMapping("/add/ir/qa")
    @ResponseBody
    public ResponseEntity add(
            @RequestParam(name = "question") String question,
            @RequestParam(name = "answer") String answer
    ) {
        try {
            XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                    .field("question", question)
                    .field("answer", answer)
                    .endObject();
            IndexResponse result = this.client.prepareIndex(INDEX, TYPE)
                    .setSource(content)
                    .get();
            return new ResponseEntity(result.getId(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
