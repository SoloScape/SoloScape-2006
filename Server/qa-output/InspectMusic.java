import com.rs2.cache.js5.*;
import com.rs2.model.music.*;
import com.rs2.net.packet.*;
import java.io.*;
import java.util.*;
public class InspectMusic {
 public static void main(String[] args) throws Exception {
 Interfaces.load(); MusicTrackDefinition.loadDefinitions();
 try (Js5CacheStore s = new Js5CacheStore(new File("cache"))) {
 Js5ReferenceTable t=s.readReferenceTable(6);
 for (Interfaces.Component c:Interfaces.group(239).values()) {
 if(c.type!=4 || c.actionType!=1) continue;
 byte[] b=c.getData(); String raw=new String(b,"ISO-8859-1");
 int id=MusicManager.trackIdForRevision443Child(c.childId);
 System.out.println(c.childId+" legacy="+id+" raw="+Arrays.toString(b)+" strings="+raw.replaceAll("[^\\x20-\\x7e]","|"));
 }
 System.out.println("homescape asset="+t.getGroupId("homescape"));
 }
 }
}
