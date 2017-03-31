package UI;

/**
 * Created by Akihiro on 2017/03/04.
 */
public class CorePartLayerDatas {
    private LayerDataEx f_b_rinkaku;
    private LayerDataEx f_b_kami;
    private LayerDataEx r_e_kurome;
    private LayerDataEx r_e_matsuge;
    private LayerDataEx r_e_kirikuchi;
    private LayerDataEx l_e_kurome;
    private LayerDataEx l_e_matsuge;
    private LayerDataEx l_e_kirikuchi;
    private LayerDataEx r_e_b_mayu;
    private LayerDataEx l_e_b_mayu;
    private LayerDataEx m_mouth;
    private LayerDataEx m_shita;

    public CorePartLayerDatas(){
        f_b_rinkaku = new LayerDataEx("f_b_rinkaku", LayerData.LayerDataType.NullNull);
        f_b_kami    = new LayerDataEx("f_b_kami", LayerData.LayerDataType.NullNull);
        r_e_kurome  = new LayerDataEx("r_e_kurome", LayerData.LayerDataType.NullNull);
        r_e_matsuge  = new LayerDataEx("r_e_mabuta", LayerData.LayerDataType.NullNull);
        r_e_kirikuchi = new LayerDataEx("r_e_kirikuchi", LayerData.LayerDataType.NullNull);
        l_e_kurome  = new LayerDataEx("l_e_kurome", LayerData.LayerDataType.NullNull);
        l_e_matsuge  = new LayerDataEx("l_e_mabuta", LayerData.LayerDataType.NullNull);
        l_e_kirikuchi = new LayerDataEx("l_e_kirikuchi", LayerData.LayerDataType.NullNull);
        r_e_b_mayu  = new LayerDataEx("r_e_b_mayu", LayerData.LayerDataType.NullNull);
        l_e_b_mayu  = new LayerDataEx("l_e_b_mayu", LayerData.LayerDataType.NullNull);
        m_mouth     = new LayerDataEx("m_mouth", LayerData.LayerDataType.NullNull);
        m_shita     = new LayerDataEx("m_shita", LayerData.LayerDataType.NullNull);
    }

    public LayerDataEx getF_b_kami() {
        return f_b_kami;
    }

    public LayerDataEx getF_b_rinkaku() {
        return f_b_rinkaku;
    }

    public LayerDataEx getL_e_b_mayu() {
        return l_e_b_mayu;
    }

    public LayerDataEx getL_e_kirikuchi() {
        return l_e_kirikuchi;
    }

    public LayerDataEx getL_e_kurome() {
        return l_e_kurome;
    }

    public LayerDataEx getL_e_matsuge() {
        return l_e_matsuge;
    }

    public LayerDataEx getM_mouth() {
        return m_mouth;
    }

    public LayerDataEx getM_shita() {
        return m_shita;
    }

    public LayerDataEx getR_e_b_mayu() {
        return r_e_b_mayu;
    }

    public LayerDataEx getR_e_kirikuchi() {
        return r_e_kirikuchi;
    }

    public LayerDataEx getR_e_kurome() {
        return r_e_kurome;
    }

    public LayerDataEx getR_e_matsuge() {
        return r_e_matsuge;
    }

    public LayerDataEx getLayerData(String name){
        switch (name){
            case "f_b_rinkaku":
                return f_b_rinkaku;
            case "f_b_kami":
                return f_b_kami;
            case "r_e_kurome":
                return r_e_kurome;
            case "r_e_matsuge":
                return r_e_matsuge;
            case "r_e_kirikuchi":
                return r_e_kirikuchi;
            case "l_e_kurome":
                return l_e_kurome;
            case "l_e_matsuge":
                return l_e_matsuge;
            case "l_e_kirikuchi":
                return l_e_kirikuchi;
            case "r_e_b_mayu":
                return r_e_b_mayu;
            case "l_e_b_mayu":
                return l_e_b_mayu;
            case "m_mouth":
                return m_mouth;
            case "m_shita":
                return m_shita;
                default:
                    return null;
        }
    }

    public LayerDataEx[] getLayerDataExArray(){
        LayerDataEx[] array = {
                f_b_rinkaku, f_b_kami,
                r_e_kurome, r_e_matsuge, r_e_kirikuchi,
                l_e_kurome, l_e_matsuge, l_e_kirikuchi,
                r_e_b_mayu,
                l_e_b_mayu,
                m_mouth, m_shita
        };
        return array;
    }

}
