import com.rs2.ServerSettings;
import com.rs2.cache.js5.Interfaces;
import com.rs2.model.player.Player;
import com.rs2.model.item.ItemStack;
import com.rs2.model.skill.smithing.SmithingBarDefinition;
import com.rs2.model.skill.smithing.SmithableItemDefinition;
import com.rs2.net.IsaacCipher;
import com.rs2.net.packet.InterfaceBridge;
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
public class SmithingInterfaceChecks {
 public static void main(String[] args) throws Exception {
  ServerSettings.clientBuild=443;
  Interfaces.load();
  checkDecemberRecipes();
  checkInterfaces();
  // Simulate HotSwap: method code is current, but the initialized maps are old.
  removeSmithingMappings("COMPONENTS", false);
  removeSmithingMappings("LEGACY_COMPONENTS", true);
  checkInterfaces();
  System.out.println("Smithing interface checks passed (fresh and hot-reloaded maps, all six metals)."); System.exit(0);
 }
 private static void removeSmithingMappings(String name, boolean reverse) throws Exception {
  Field table=InterfaceBridge.class.getDeclaredField(name); table.setAccessible(true);
  Object wrapper=table.get(null);
  Field backing=wrapper.getClass().getDeclaredField("m"); backing.setAccessible(true);
  java.util.Map<Integer,Integer> entries=(java.util.Map<Integer,Integer>)backing.get(wrapper);
  java.util.Iterator<java.util.Map.Entry<Integer,Integer>> iterator=entries.entrySet().iterator();
  while(iterator.hasNext()) {
   java.util.Map.Entry<Integer,Integer> entry=iterator.next();
   if((reverse?entry.getKey():entry.getValue()) >>> 16 == 312) iterator.remove();
  }
 }
 private static void checkInterfaces() throws Exception {
  for(int id:SmithingBarDefinition.productNameTextIds) require(Interfaces.forId(312, InterfaceBridge.translate(id) & 65535).type==4,"Missing name widget "+id);
  for(int id:SmithingBarDefinition.barRequirementTextIds) require(Interfaces.forId(312, InterfaceBridge.translate(id) & 65535).type==4,"Missing bar widget "+id);
  Field socket=Player.class.getDeclaredField("socketChannel"); socket.setAccessible(true);
  try(ServerSocketChannel listener=ServerSocketChannel.open(); Socket client=new Socket()) {
   listener.bind(new java.net.InetSocketAddress("127.0.0.1",0)); client.connect(listener.getLocalAddress()); client.setSoTimeout(2000);
   try(SocketChannel transport=listener.accept()) {
    Player p=new Player(null); socket.set(p,transport); p.setOutboundCipher(new IsaacCipher(new int[4]));
    IsaacCipher decoder=new IsaacCipher(new int[4]); DataInputStream in=new DataInputStream(client.getInputStream());
    for(SmithingBarDefinition bar:SmithingBarDefinition.VALUES) {
     for(int i=0;i<bar.getSmithableItems().length;i++) {
      SmithableItemDefinition product=bar.getSmithableItems()[i];
      p.packetSender.sendInterfaceSlotItem(product==null?null:new ItemStack(product.getProductItemId(),product.getOutputAmount()),SmithingBarDefinition.productItemSlots[i],SmithingBarDefinition.productItemInterfaceIds[i],product==null?0:product.getOutputAmount());
      require(((in.readUnsignedByte()-decoder.nextInt())&255)==213,"No icon update");
      int length=in.readUnsignedShort(); byte[] payload=new byte[length]; in.readFully(payload);
      java.nio.ByteBuffer bytes=java.nio.ByteBuffer.wrap(payload);
      int widget=bytes.getInt(); bytes.getShort(); int slot=bytes.get()&255; int item=bytes.getShort()&65535;
      Interfaces.Component c=Interfaces.forId(widget >>> 16, widget & 65535);
      require(c!=null && c.type==2 && slot<c.width*c.height,"Invalid icon target");
      int legacyClick=InterfaceBridge.toLegacyComponent(widget);
      require(legacyClick>=1119 && legacyClick<=1123,"Item click mapping missing");
      require(item==(product==null?0:product.getProductItemId()+1),"Wrong icon item");
      if(i==21 || i==22) require(slot==0 && c.childId==(i==21?166:170),"Special icon misplaced");
      if(i==19) require(slot==3 && c.childId==150,"Other item should be below knives");
      if(i==23) require(slot==4 && c.childId==149,"Nails icon misplaced");
      if(i==25 || i==26) require(c.childId==151 && slot==i-25,"Crossbow icon misplaced");
      if(i==27) require(c.childId==180 && slot==0,"Grapple icon misplaced");
     }
    }
   }
  }
 }
 private static void checkDecemberRecipes() {
  int[] bars={2349,2351,2353,2359,2361,2363};
  int[] bolts={9375,9377,9378,9379,9380,9381};
  int[] limbs={9420,9423,9425,9427,9429,9431};
  int[] boltLevels={3,18,33,53,73,88};
  int[] limbLevels={6,23,36,56,76,91};
  for(int i=0;i<bars.length;i++) {
   SmithableItemDefinition[] items=SmithingBarDefinition.forBarItemId(bars[i]).getSmithableItems();
   require(items[25].getProductItemId()==bolts[i] && items[25].getRequiredLevel()==boltLevels[i]
       && items[25].getOutputAmount()==10 && items[25].getBarCount()==1,"Incorrect bolt recipe");
   require(items[26].getProductItemId()==limbs[i] && items[26].getRequiredLevel()==limbLevels[i]
       && items[26].getOutputAmount()==1 && items[26].getBarCount()==1,"Incorrect limb recipe");
   require((items[27]!=null)==(bars[i]==2359),"Only mithril makes grapple tips");
   require(items[23]!=null && items[23].getDisplayName().equals("Nails"),"Nails missing");
   require((items[24]!=null)==(bars[i]==2353),"Only steel has studs");
  }
  SmithableItemDefinition[] bronze=SmithingBarDefinition.forBarItemId(2349).getSmithableItems();
  require(bronze[19]==SmithableItemDefinition.BRONZE_WIRE && bronze[22]==null,"Incorrect bronze miscellaneous layout");
  SmithableItemDefinition grapple=SmithingBarDefinition.forBarItemId(2359).getSmithableItems()[27];
  require(grapple.getProductItemId()==9416 && grapple.getRequiredLevel()==59 && grapple.getBarCount()==1,"Incorrect grapple recipe");
  ServerSettings.clientBuild=317;
  require(SmithingBarDefinition.forBarItemId(2349).getSmithableItems().length==25,"Legacy layout changed");
  ServerSettings.clientBuild=443;
 }
 static void require(boolean ok,String message) {if(!ok) throw new AssertionError(message);}
}

