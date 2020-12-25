package ir.pepotec.app.bluetoothremote

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_channel_control.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentChannelControl : Fragment(), SeekBar.OnSeekBarChangeListener {

    lateinit var ctx: ActivityMain

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_channel_control, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        //runSender()
    }

    private fun runSender() {
        CoroutineScope(IO).launch {
            while (true) {
                withContext(Main) {
                    sendDataToMicro(sbarA)
                    sendDataToMicro(sbarB)
                    sendDataToMicro(sbarC)
                    sendDataToMicro(sbarD)
                }
                delay(250)
            }
        }
    }

    private fun init() {
      sbarA.setOnSeekBarChangeListener(this)
      sbarB.setOnSeekBarChangeListener(this)
      sbarC.setOnSeekBarChangeListener(this)
      sbarD.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        sendDataToMicro(seekBar)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    private fun sendDataToMicro(seekBar: SeekBar?) {
        var data = ""
        val progress = seekBar?.progress.toString()
        when (seekBar?.id) {
            R.id.sbarA -> {
                txtAValue.text = progress
                data += "a-"
            }
            R.id.sbarB -> {
                txtBValue.text = progress
                data += "b-"
            }
            R.id.sbarC -> {
                txtCValue.text = progress
                data += "c-"
            }
            R.id.sbarD -> {
                txtDValue.text = progress
                data += "d-"
            }
        }
        data += progress
        ctx.connectedThread.write(data)
    }

}