package Protocols;

public class IPv4 {
    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    //return true if given address is correct
    public static boolean is_ip_valid(long ip_address){
        if(ip_address > 4294967295L || ip_address < 0) {
            return false;
        }
        return true;
    }
    public static boolean is_ip_valid(String ip_address){

        return true;
    }

    //return true if given net mask is correct
    public static boolean is_mask_valid(long net_mask){

        return true;
    }
    public static boolean is_mask_valid(String net_mask){

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

    //1. parse mask from String dotted format or short mask format(xx,/xx) to long number
    public static long parse_mask_to_long(String net_mask_string){
        // /xx format to long number
        if(net_mask_string.length()<=3){
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

            //dotted String format to long number
        }else{
            return parse_to_long(net_mask_string);
        }
    }
    //2. parse mask to String dotted format:

    //2a. parse mask from long number format to String dotted
    public static String parse_mask_to_string_dot(long net_mask_long){
        return parse_to_string(net_mask_long);
    }
    //2b. parse mask from short mask format(/xx,xx) to String dotted
    public static String parse_mask_to_string_dot(String net_short_mask){
        long to_return = parse_mask_to_long(net_short_mask);
        return parse_to_string(to_return);
    }

    //3. parse mask to String short format(/xx,xx)- it returns mask without '/', in order to get a slash add second 'true'
    //parameter

    //3a. parse mask from long number format to String short mask format without slash(xx):
    public static String parse_mask_to_string_short(long net_mask_long){
        //net_mask_long = 23124124;
        //parse to binary string:
        String net_mask_binary = Long.toBinaryString(net_mask_long);
        //counting 1s to get size of mask:
        char[] mask = net_mask_binary.toCharArray();
        int counter = 0;
        for(int i = 0; i < net_mask_binary.length(); i++){
            if(mask[i] == '1'){
                counter++;
            }
        }
        return String.valueOf(counter);
    }
    //3b. parse mask from long number format to String short mask format with slash (/xx):
    public static String parse_mask_to_string_short(long net_mask_long, boolean slash){
        if(slash)
            return "/" + parse_mask_to_string_short(net_mask_long);
        else
            return parse_mask_to_string_short(net_mask_long);
    }

    //4. parse mask from String dotted format to String short mask format- it returns mask without '/', in order to
    //get a slash add second 'true' parameter:

    //4a. parse mask from String dotted format to String short mask without slash ("xx")
    public static String parse_mask_to_string_short(String net_mask){
        return parse_mask_to_string_short(parse_to_long(net_mask));
    }
    //4b. parse mask from String dotted format to String short mask with slash ("/xx")
    public static String parse_mask_to_string_short(String net_mask, boolean slash){
        return parse_mask_to_string_short(parse_to_long(net_mask), slash);
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
