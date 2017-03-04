package UI;

/**
 * Created by Akihiro on 2017/03/04.
 */
public class CorePartLayerDatas {
    private LayerDataEx f_b_rinkaku;
    private LayerDataEx r_e_kurome;
    private LayerDataEx r_e_mabuta;
    private LayerDataEx l_e_kurome;
    private LayerDataEx l_e_mabuta;
    private LayerDataEx r_e_b_mayu;
    private LayerDataEx l_e_b_mayu;
    private LayerDataEx m_mouth;

    public CorePartLayerDatas(){
        f_b_rinkaku = new LayerDataEx("f_b_rinkaku", LayerData.LayerDataType.NullNull);
        r_e_kurome = new LayerDataEx("r_e_kurome", LayerData.LayerDataType.NullNull);
        r_e_mabuta = new LayerDataEx("r_e_mabuta", LayerData.LayerDataType.NullNull);
        l_e_kurome = new LayerDataEx("l_e_kurome", LayerData.LayerDataType.NullNull);
        l_e_mabuta = new LayerDataEx("l_e_mabuta", LayerData.LayerDataType.NullNull);
        r_e_b_mayu = new LayerDataEx("r_e_b_mayu", LayerData.LayerDataType.NullNull);
        l_e_b_mayu = new LayerDataEx("l_e_b_mayu", LayerData.LayerDataType.NullNull);
        m_mouth = new LayerDataEx("m_mouth", LayerData.LayerDataType.NullNull);
    }

    public LayerDataEx getLayerData(String name){
        switch (name){
            case "f_b_rinkaku":
                return f_b_rinkaku;
            case "r_e_kurome":
                return r_e_kurome;
            case "r_e_mabuta":
                return r_e_mabuta;
            case "l_e_kurome":
                return l_e_kurome;
            case "l_e_mabuta":
                return l_e_mabuta;
            case "r_e_b_mayu":
                return r_e_b_mayu;
            case "l_e_b_mayu":
                return l_e_b_mayu;
            case "m_mouth":
                return m_mouth;
                default:
                    return null;
        }
    }

}
