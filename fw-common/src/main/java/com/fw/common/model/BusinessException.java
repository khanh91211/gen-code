package com.fw.common.model;

import com.fw.common.model.enumeration.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessException extends Exception {
  private ErrorEnum error;
  private Object additionalData;
  private static final long serialVersionUID = 8442358956304045349L;

  public BusinessException(ErrorEnum error) {
    this.error = error;
  }
}
