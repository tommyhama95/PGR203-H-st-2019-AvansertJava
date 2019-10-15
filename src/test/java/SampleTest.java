import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class SampleTest {

  @Test
  void dummyTest(){
    String[] things = new String[]{"SampleA","SampleB","SampleC"};
    List<String> sampleList = new ArrayList<>(Arrays.asList(things));
    assertThat(sampleList).contains(things[new Random().nextInt(things.length)]);
  }
}
