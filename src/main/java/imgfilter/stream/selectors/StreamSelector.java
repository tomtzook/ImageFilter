package imgfilter.stream.selectors;

import imgfilter.stream.ImageStream;

import java.util.Optional;

public interface StreamSelector {

    Optional<ImageStream> selectNew() throws Exception;
}
