package Protocols;

public class IPv4 {
    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    //return true if given address is correct
    public static boolean is_mask_valid(long ip_mask){

        return true;
    }

    //parse ip address from String dotted format to the long number
    public static long parse_to_long(String ip_address_string){
        ip_address_string = ip_address_string.trim();
        //split ip_address_string into String ip_octets
        String[] ip_octets_string = ip_address_string.split("\\.");

        //parse octets(str) to long_octets and shift each of them in left, eventually sum up to ip_address_long
        long[] ip_octets_long = new long[4];
        long ip_address_long = 0;
        for(int i = 0; i < 4; i++){
            ip_octets_long[i] = Integer.parseInt(ip_octets_string[i]);
            ip_octets_long[i] = ip_octets_long[i] << (24-8*i);
            ip_address_long += ip_octets_long[i];
        }

        return ip_address_long;
    }

    //parse ip address from long number to String dotted format
    public static String parse_to_string(long ip_address_long){
        //change from decimal long to binary string
        String ip_address_string = Long.toBinaryString(ip_address_long);
        //add 0s to beginning of the binary address if it's length is different from 32
        if(ip_address_string.length() < 32){
            int to_add =  32-ip_address_string.length();
            String zeros = "0";
            for(int i = 0; i < to_add-1; i++){
                zeros = zeros.concat("0");
            }
            ip_address_string = zeros + ip_address_string;
        }
        //inserting dots in specific position (index 8, 17, 26) in order to distinguish binary octets
        StringBuilder ip_string_builder = new StringBuilder(ip_address_string);
        for(int i = 1; i < 4; i++){
           ip_string_builder.insert(i*8 + (i-1),".");
        }
        //converting binary strings to decimal ints
        ip_address_string = ip_string_builder.toString();
        int[] ip_octets_int = new int[4];
        String[] ip_octets_string = ip_address_string.split("\\.");
        for(int i = 0; i < 4; i++){
            ip_octets_int[i] = Integer.parseInt(ip_octets_string[i], 2);
            ip_octets_string[i] = String.valueOf(ip_octets_int[i]);
        }
        //converting decimal strings into one string ip dotted address
        ip_address_string = ip_octets_string[0];
        for(int i = 0; i < 3; i++){
            ip_address_string += "." + ip_octets_string[i+1];
        }

        return ip_address_string;
    }

    //parse ipv4 mask from String format "/xx" or just "xx" to long number
    public static long smask_string_to_long(String net_mask_string){
        //net_mask = "/24" - usun to
        //delete '/' if was given
        StringBuilder net_mask_builder = new StringBuilder(net_mask_string);
        if(net_mask_string.charAt(0) == '/'){
            net_mask_builder.deleteCharAt(0);
        }
        //convert mask to binary format
        int mask = Integer.valueOf(net_mask_builder.toString());
        net_mask_string = "";
        for(int i = 0; i < mask; i++){
            net_mask_string = net_mask_string.concat("1");
        }
        String zeros = "0";
        for(int i = 0; i < 31-mask; i++){
            zeros = zeros.concat("0");
        }
        if(mask != 32) {
            net_mask_string = net_mask_string + zeros;
        }
        //convert String binary mask format to long number

        return Long.parseLong(net_mask_string, 2);
    }


    //create default IPv4 packet
    public static IPv4Packet create_packet(){
        return new IPv4Packet();
    }

    //create IPv4 packet
    public static IPv4Packet create_packet(long source_address, long destination_address, int time_to_live, Data data){
        return new IPv4Packet(source_address, destination_address, time_to_live, data);
    }
}
