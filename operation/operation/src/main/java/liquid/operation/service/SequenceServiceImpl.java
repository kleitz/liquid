package liquid.operation.service;

import liquid.operation.domain.Sequence;
import liquid.operation.repository.SequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by redbrick9 on 4/26/14.
 */
@Service
public class SequenceServiceImpl implements SequenceService{

    @Autowired
    private SequenceRepository sequenceRepository;

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    public long getNextValue(String name) {
        sequenceRepository.increment(name);
        long value = sequenceRepository.getValue();
        if (0L == value) {
            Sequence sequence = new Sequence();
            sequence.setName(name);
            sequence.setPrefix(name);
            sequence.setValue(0L);
            sequenceRepository.save(sequence);
            sequenceRepository.increment(name);
            value = sequenceRepository.getValue();
        }
        return value;
    }
}
