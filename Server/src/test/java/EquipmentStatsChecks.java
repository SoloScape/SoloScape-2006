import com.rs2.ServerSettings;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.item.ItemDefinition;
import com.rs2.model.item.ItemStack;
import com.rs2.model.player.Player;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.*;
import com.rs2.net.packet.handler.InterfaceActionPacketHandler;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
public final class EquipmentStatsChecks {
 public static void main(String[] args) throws Exception {
  ServerSettings.clientBuild=443;
  Interfaces.load(); com.rs2.model.quest.QuestDefinition.loadDefinitions(); ItemDefinition.loadDefinitions();
  require(ItemDefinition.forId(1205).getBonuses()[0]>0,"Dagger bonuses not loaded");
  Field socket=Player.class.getDeclaredField("socketChannel"); socket.setAccessible(true);
  for (int stage : new int[] {1, 41}) {
  try(SocketChannel transport=SocketChannel.open()) {
   transport.close();
   Player player=new Player(null); socket.set(player,transport);
   player.setOutboundCipher(new IsaacCipher(new int[4])); player.setQuestState(0,stage);
   player.getEquipmentManager().getContainer().add(new ItemStack(1205,1),3);
   ByteBuffer payload=ByteBuffer.allocate(4).putInt(25362456); payload.flip();
   new InterfaceActionPacketHandler().handle(player,new IncomingPacket(ClientPackets.INTERFACE_BUTTON,4,PacketBuffer.wrapReader(payload)));
   require(player.getOpenInterfaceId()==15106,"Stats button did not open interface");
   require(InterfaceBridge.translateGroup(15106)==465,"Wrong native stats group");
   int items=InterfaceBridge.translate(15107);
   require(Interfaces.forPackedId(items).type==2 && Interfaces.forPackedId(items).width*Interfaces.forPackedId(items).height>=14,"Equipment icons missing");
   require(InterfaceBridge.toLegacyComponent(items)==1688,"Equipment removal mapping missing");
   for(int i=0;i<12;i++) require(((Integer)player.getCombatBonuses().get(i)).intValue()==ItemDefinition.forId(1205).getBonuses()[i],"Incorrect equipped bonus "+i);
   player.getEquipmentManager().getContainer().remove(new ItemStack(1205,1));
   player.getEquipmentManager().refresh();
   for(int i=0;i<12;i++) require(((Integer)player.getCombatBonuses().get(i)).intValue()==0,"Bonus stale after removal");
   new com.rs2.net.packet.handler.CloseInterfacePacketHandler().handle(player, new IncomingPacket(ClientPackets.CLOSE_INTERFACE,0,null));
   require(player.getOpenInterfaceId()==(stage==1?0:6179),"Incorrect stats close behavior");
  }
  }
  System.out.println("Equipment stats checks passed (exact logged button, native screen, icons, bonuses, removal)."); System.exit(0);
 }
 static void require(boolean value,String message) {if(!value) throw new AssertionError(message);}
}





