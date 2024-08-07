package com.carumuch.capstone.global.validation;

import jakarta.validation.GroupSequence;
import static com.carumuch.capstone.global.validation.ValidationGroups.*;

@GroupSequence({
        NotBlankGroup.class,
        NotNullGroup.class,
        PatternGroup.class,
        SizeGroup.class,
        EmailGroup.class
})
public interface ValidationSequence {
}
