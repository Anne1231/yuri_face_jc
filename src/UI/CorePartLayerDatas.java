package UI;

/**
 * Created by Akihiro on 2017/03/04.
 */
public class CorePartLayerDatas {
    LayerData f_b_rinkaku;
    LayerData r_e_kurome;
    LayerData r_e_mabuta;
    LayerData l_e_kurome;
    LayerData l_e_mabuta;
    LayerData r_e_b_mayu;
    LayerData l_e_b_mayu;
    LayerData m_mouth;

    public CorePartLayerDatas(){
        f_b_rinkaku = new LayerData("f_b_rinkaku", LayerData.LayerDataType.NullNull);
        r_e_kurome = new LayerData("r_e_kurome", LayerData.LayerDataType.NullNull);
        r_e_mabuta = new LayerData("r_e_mabuta", LayerData.LayerDataType.NullNull);
        l_e_kurome = new LayerData("l_e_kurome", LayerData.LayerDataType.NullNull);
        l_e_mabuta = new LayerData("l_e_mabuta", LayerData.LayerDataType.NullNull);
        r_e_b_mayu = new LayerData("r_e_b_mayu", LayerData.LayerDataType.NullNull);
        l_e_b_mayu = new LayerData("l_e_b_mayu", LayerData.LayerDataType.NullNull);
        m_mouth = new LayerData("m_mouth", LayerData.LayerDataType.NullNull);
    }
    
}
