package unpackaged;
import jagex.io.FrameBuffer;
import jagex.io.JSocket;
import jagex.utils.SubNode;
import jagex.world.actors.StillGraphic;
import java.lang.reflect.Field;
public final class EquipmentStatsCloseChecks {
 public static void main(String[] args) throws Exception {
  Field mode=JSocket.class.getDeclaredField("tutorialInstructionMode"); mode.setAccessible(true);
  Class39_Sub5_Sub4.widgetsLoaded=new boolean[500];
  for(boolean tutorial:new boolean[]{false,true}) {
   mode.setBoolean(null,tutorial);
   FrameBuffer.outgoingGameBuffer=new FrameBuffer(32);
   FrameBuffer.outgoingGameBuffer.initIsaacCipher(new int[4]);
   Class55.characterDesignActive=false;
   Class39_Sub11.anInt1478=465;
   Class39_Sub5_Sub14.anInt1912=214;
   StillGraphic.anInt2338=-1; SubNode.anInt1348=-1; ClientScript.anInt1713=-1;
   Class55.method999(31121);
   require(Class39_Sub11.anInt1478==-1,"Stats viewport did not close");
   require(Class39_Sub5_Sub14.anInt1912==(tutorial?214:-1),"Incorrect tutorial chatbox retention");
   require(FrameBuffer.outgoingGameBuffer.offset==1,"Expected one close notification");
  }
  System.out.println("Equipment stats close checks passed (tutorial retains instructions; normal gameplay closes chatbox).");
 }
 static void require(boolean value,String message) {if(!value) throw new AssertionError(message);}
}
