package gemmini

import org.chipsalliance.cde.config.{Config, Parameters}
import chisel3._
import freechips.rocketchip.diplomacy.LazyModule
import freechips.rocketchip.subsystem.SystemBusKey
import freechips.rocketchip.tile.BuildRoCC

object GemminiCustomConfigs {
  // Default configurations
  val defaultConfig = GemminiConfigs.defaultConfig
  val defaultFpConfig = GemminiFPConfigs.BF16DefaultConfig

  val noscale = Some(ScaleArguments(
      (t: SInt, f: Float) => t,
      1, Float(8, 24), -1, identity = "1.0",
      c_str = "(x)"
  ))

  val noscalefp = Some(ScaleArguments(
      (t: Float, f: Float) => t,
      1, Float(8, 24), -1, identity = "1.0",
      c_str = "(x)"
  ))

  // Create your own configs here
  val baselineInferenceConfig = defaultConfig.copy(
    has_training_convs = false,
    meshRows = 4,
    meshColumns = 4,
    dataflow = Dataflow.WS,
    max_in_flight_mem_reqs = 64,  
    acc_read_full_width = false,
    ex_read_from_acc = false,
    ex_write_to_spad = false,
    hardcode_d_to_garbage_addr = true,
    mvin_scale_args = noscale,
    acc_scale_args = noscale,
  )

  val baselineInferenceFpConfig = defaultFpConfig.copy(
    has_training_convs = false,
    meshRows = 4,
    meshColumns = 4,
    dataflow = Dataflow.WS,
    max_in_flight_mem_reqs = 64,
    acc_read_full_width = false,
    ex_read_from_acc = false,
    ex_write_to_spad = false,
    hardcode_d_to_garbage_addr = true,
    mvin_scale_args = noscalefp,
    acc_scale_args = noscalefp,
    dma_buswidth = 64
  )

  val highPerfInferenceConfig = defaultConfig.copy(
    meshRows = 32,
    meshColumns = 32,

    has_training_convs = false,

    sp_capacity = CapacityInKilobytes(512),
    acc_capacity = CapacityInKilobytes(128),
  )

  val trainingConfig = defaultFpConfig.copy(
    inputType = Float(expWidth = 8, sigWidth = 24),
    accType = Float(expWidth = 8, sigWidth = 24),

    meshRows = 8,
    meshColumns = 8,

    has_training_convs = true,
    has_max_pool =  false,

    sp_capacity = CapacityInKilobytes(512),
    acc_capacity = CapacityInKilobytes(128),
  )

  val ibertInferenceConfig = defaultConfig.copy(
    has_training_convs = false,
    has_max_pool =  false,
    has_normalizations = true,

    acc_capacity = CapacityInKilobytes(128),
  )

  // Specify which of your custom configs you want to build here
  val customConfig = baselineInferenceFpConfig
}


class GemminiCustomConfig[T <: Data : Arithmetic, U <: Data, V <: Data](
  gemminiConfig: GemminiArrayConfig[T,U,V] = GemminiCustomConfigs.customConfig
) extends Config((site, here, up) => {
  case BuildRoCC => up(BuildRoCC) ++ Seq(
    (p: Parameters) => {
      implicit val q = p
      val gemmini = LazyModule(new Gemmini(gemminiConfig))
      gemmini
    }
  )
})

