package com.carumuch.capstone.global.validation;

import jakarta.validation.GroupSequence;
import static com.carumuch.capstone.global.validation.ValidationGroups.*;

@GroupSequence({
        NotBlankGroup.class,
        PatternGroup.class,
        SizeGroup.class,
        EmailGroup.class
})
public interface ValidationSequence {
}
