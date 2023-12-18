package core.user.api.service;

import core.user.api.model.SeqTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SeqService {

    private final R2dbcEntityTemplate template;

    public Mono<String> getNextCode(String seqName, int format) {
        return template.select(SeqTable.class)
                .matching(Query.query(where("option").is(seqName)))
                .first()
                .flatMap(seqTable -> {
                    int nextValue = seqTable.getSeqNo() + 1;
                    seqTable.setSeqNo(nextValue);
                    return template.update(seqTable)
                            .map(updatedSeqTable -> String.format("%0" + format + "d", updatedSeqTable.getSeqNo()));

                }).switchIfEmpty(createNewSequence(seqName, format));
    }

    private Mono<String> createNewSequence(String seqName, int format) {
        var seq = SeqTable.builder().option(seqName).period("-").seqNo(1).build();
        return template.insert(seq)
                .map(savedSeqTable -> String.format("%0" + format + "d", savedSeqTable.getSeqNo()));
    }


}
