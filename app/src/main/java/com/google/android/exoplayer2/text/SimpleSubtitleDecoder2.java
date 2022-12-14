/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer2.text;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.utils.CharsetDetector;
import com.google.android.exoplayer2.utils.CharsetMatch;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Base class for subtitle parsers that use their own decode thread.
 */
public abstract class SimpleSubtitleDecoder2 extends
    SimpleDecoder<SubtitleInputBuffer, SubtitleOutputBuffer, SubtitleDecoderException> implements
    SubtitleDecoder {

  private final String name;

  /** @param name The name of the decoder. */
  @SuppressWarnings("nullness:method.invocation.invalid")
  protected SimpleSubtitleDecoder2(String name) {
    super(new SubtitleInputBuffer[2], new SubtitleOutputBuffer[2]);
    this.name = name;
    setInitialInputBufferSize(1024);
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public void setPositionUs(long timeUs) {
    // Do nothing
  }

  @Override
  protected final SubtitleInputBuffer createInputBuffer() {
    return new SubtitleInputBuffer();
  }

  @Override
  protected final SubtitleOutputBuffer createOutputBuffer() {
    return new SimpleSubtitleOutputBuffer(this::releaseOutputBuffer);
  }

  @Override
  protected final SubtitleDecoderException createUnexpectedDecodeException(Throwable error) {
    return new SubtitleDecoderException("Unexpected decode error", error);
  }

  @SuppressWarnings("ByteBufferBackingArray")
  @Override
  @Nullable
  protected final SubtitleDecoderException decode(
      SubtitleInputBuffer inputBuffer, SubtitleOutputBuffer outputBuffer, boolean reset) {
    try {
      ByteBuffer inputData = Assertions.checkNotNull(inputBuffer.data);
      CharsetDetector detector = new CharsetDetector();
      detector.setText(inputData.array());
      CharsetMatch charsetMatch = detector.detect();
      byte[] array = charsetMatch.getString().getBytes();
      Log.i("Subtitle Decoding","Decoded Successfully");
      Subtitle subtitle = decode(array, array.length, reset);
      //Set content to subtitle after setting correct encoding
      //This way we have fixed characters for windows-1256 encoding
      outputBuffer.setContent(inputBuffer.timeUs, subtitle, inputBuffer.subsampleOffsetUs);
      // Clear BUFFER_FLAG_DECODE_ONLY (see [Internal: b/27893809]).
      outputBuffer.clearFlag(C.BUFFER_FLAG_DECODE_ONLY);
      return null;
    } catch (SubtitleDecoderException e) {
      return e;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Decodes data into a {@link Subtitle}.
   *
   * @param data An array holding the data to be decoded, starting at position 0.
   * @param size The size of the data to be decoded.
   * @param reset Whether the decoder must be reset before decoding.
   * @return The decoded {@link Subtitle}.
   * @throws SubtitleDecoderException If a decoding error occurs.
   */
  protected abstract Subtitle decode(byte[] data, int size, boolean reset)
      throws SubtitleDecoderException;

}
