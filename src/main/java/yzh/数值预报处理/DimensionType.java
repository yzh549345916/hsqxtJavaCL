package yzh.数值预报处理;

public enum DimensionType {
    /// <summary>
    /// X dimension
    /// </summary>
    X,
    /// <summary>
    /// Y dimension
    /// </summary>
    Y,
    /// <summary>
    /// Z/Level dimension
    /// </summary>
    Z,
    /// <summary>
    /// Time dimension
    /// </summary>
    T,
    E,    //Ensemble dimension
    /// <summary>
    /// Xtrack dimension - for HDF EOS swath data
    /// </summary>
    Xtrack,
    /// <summary>
    /// Other dimension
    /// </summary>
    Other
}