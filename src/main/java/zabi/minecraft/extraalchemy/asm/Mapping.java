package zabi.minecraft.extraalchemy.asm;

public class Mapping {
	public String name_mcp;
	public String name_srg;
	public String name_obf;
	public String desc_srg;
	public String desc_obf;
	public String name;
	

	public static final Mapping v1 = new Mapping();
	public static final Mapping v2 = new Mapping();
	
	static {
		v1.name_mcp="hasEffect";
		v1.name_srg="func_77636_d";
		v1.name_obf="f_";
		v1.desc_srg="(Lnet/minecraft/item/ItemStack;)Z";
		v1.desc_obf="(Lain;)Z";
		v1.name = "Old mappings";

		v2.name_mcp="hasEffect";
		v2.name_srg="func_77962_s";
		v2.name_obf="f_";
		v2.desc_srg="(Lnet/minecraft/item/ItemStack;)Z";
		v2.desc_obf="(Laip;)Z";
		v2.name = "20171108";
	}
	
	public static final Mapping[] mappings = new Mapping[] {v2, v1};
	
}
