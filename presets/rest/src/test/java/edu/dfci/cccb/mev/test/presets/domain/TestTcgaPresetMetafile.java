package edu.dfci.cccb.mev.test.presets.domain;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.log4j.Log4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.dfci.cccb.mev.presets.contract.Preset;
import edu.dfci.cccb.mev.presets.contract.PresetDescriptor;
import edu.dfci.cccb.mev.presets.contract.PresetsBuilder;
import edu.dfci.cccb.mev.presets.contract.exceptions.PresetException;
import edu.dfci.cccb.mev.test.presets.rest.configuration.PresetsRestConfigurationTest;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={PresetsRestConfigurationTest.class})
public class TestTcgaPresetMetafile {
  
  private @Inject PresetsBuilder builder;
  private @Inject @Named ("tcgaPresetRoot") URL tcgaPresetRoot;
  
  @Test
  public void testTcgaPresetRoot() throws IOException{
    assertEquals("file", tcgaPresetRoot.getProtocol ());
  }
  
  @Test
  public void testGetDescriptor () throws PresetException, MalformedURLException  {
    
    Preset preset = builder.createPreset (new Object[]{"filename.tsv", "path/of/file", "NS", "Nothing Serious", "HS", "HiSeq", "Level2"});
     
    PresetDescriptor descriptor = preset.descriptor ();
    
    URL expectedDataURL = new URL(tcgaPresetRoot, "tcga_data/path/of/file/filename.tsv");    
    assertEquals (expectedDataURL, descriptor.dataUrl ());
    
    
    URL expectedColumnURL = new URL(tcgaPresetRoot, "openrefine/clinical/NS-clinical_annotations-tsv.openrefine.tar.gz");    
    assertEquals (expectedColumnURL, descriptor.columnUrl ());
    
  }
  
  @Test
  public void testToString() throws PresetException{
    Preset preset = builder.createPreset (new Object[]{"filename.tsv", "path/of/file", "NS", "Nothing Serious", "HS", "HiSeq", "Level2"});
    log.debug(preset.toString ());
    String expected = "TcgaPresetMetafile(tcgaPresetRoot="+tcgaPresetRoot.toString ()+", filename=filename.tsv, path=path/of/file, name=filename.tsv, disease=NS, diseaseName=Nothing Serious, platform=HS, platformName=HiSeq, dataLevel=Level2, descriptor=SimplePresetDescriptor(name=PRESET-filename.tsv, dataUrl="+tcgaPresetRoot.toString ()+"tcga_data/path/of/file/filename.tsv, columnUrl="+tcgaPresetRoot.toString ()+"openrefine/clinical/NS-clinical_annotations-tsv.openrefine.tar.gz))";
    log.debug (expected);
    assertEquals(expected, preset.toString ());
  }

}
