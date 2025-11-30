package com.carumuch.capstone.common.validation;

import jakarta.validation.GroupSequence;
import static com.carumuch.capstone.common.validation.ValidationGroups.*;

@GroupSequence({
        NotBlankGroup.class,
        NotNullGroup.class,
        PatternGroup.class,
        SizeGroup.class,
        EmailGroup.class
})
public interface ValidationSequence {
}
