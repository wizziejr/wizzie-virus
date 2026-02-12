// Example in Swift for iOS
import UIKit
import AVFoundation

class SpywareViewController: UIViewController {

 var captureSession = AVCaptureSession()
 var videoPreviewLayer: AVCaptureVideoPreviewLayer!
 var mediaRecorder: AVAudioRecorder!

 override func viewDidLoad() {
 super.viewDidLoad()
 setupCamera()
 setupAudioRecording()
 }

 func setupCamera() {
 captureSession.sessionPreset = .high

 guard let backCamera = AVCaptureDevice.default(for: .video) else {
 print("Unable to access back camera!")
 return
 }

 do {
 let input = try AVCaptureDeviceInput(device: backCamera)
 stillImageOutput = AVCapturePhotoOutput()
 if captureSession.canAddInput(input) && captureSession.canAddOutput(stillImageOutput) {
 captureSession.addInput(input)
 captureSession.addOutput(stillImageOutput)
 setupLivePreview()
 }
 } catch let error {
 print("Error Unable to initialize back camera: \(error.localizedDescription)")
 }
 }

 func setupLivePreview() {
 videoPreviewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
 videoPreviewLayer.videoGravity = .resizeAspect
 videoPreviewLayer.connection?.videoOrientation = .portrait
 view.layer.addSublayer(videoPreviewLayer)
 DispatchQueue.global(qos: .userInitiated).async { //[weak self] in
 self.captureSession.startRunning()
 DispatchQueue.main.async {
 self.videoPreviewLayer.frame = self.view.bounds
 }
 }
 }

 func setupAudioRecording() {
 let settings = [
 AVFormatIDKey: Int(kAudioFormatMPEG4AAC),
 AVSampleRateKey: 12000,
 AVNumberOfChannelsKey: 1,
 AVEncoderAudioQualityKey: AVAudioQuality.high.rawValue
 ] as [String : Any]

 do {
 mediaRecorder = try AVAudioRecorder(url: getDocumentsDirectory().appendingPathComponent("recording.m4a"), settings: settings)
 mediaRecorder.record()
 } catch {
 print("AudioRecorder error: \(error.localizedDescription)")
 }
 }

 func getDocumentsDirectory() -> URL {
 let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
 return paths[0]
 }
}
