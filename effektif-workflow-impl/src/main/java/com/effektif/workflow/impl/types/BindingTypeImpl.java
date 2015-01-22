/* Copyright 2014 Effektif GmbH.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
package com.effektif.workflow.impl.types;

import com.effektif.workflow.api.types.BindingType;
import com.effektif.workflow.api.types.Type;
import com.effektif.workflow.impl.plugin.ServiceRegistry;
import com.effektif.workflow.impl.type.AbstractDataType;
import com.effektif.workflow.impl.type.DataType;
import com.effektif.workflow.impl.type.DataTypeService;
import com.effektif.workflow.impl.type.InvalidValueException;


public class BindingTypeImpl extends AbstractDataType<BindingType> {
  
  protected DataType targetType;
  protected DataTypeService dataTypeService;

  public BindingTypeImpl() {
    super(BindingType.class);
  }
  
  public BindingTypeImpl(BindingType bindingTypeApi, ServiceRegistry serviceRegistry) {
    super(BindingType.class);

    this.dataTypeService = serviceRegistry.getService(DataTypeService.class);
    Type targetTypeApi = bindingTypeApi.getTargetType();
    if (targetTypeApi!=null) {
      this.targetType = dataTypeService.createDataType(targetTypeApi);
      this.valueClass = this.targetType.getValueClass();
    } 
  }

  @Override
  public Object convertJsonToInternalValue(Object apiValue) throws InvalidValueException {
    throw new UnsupportedOperationException("TODO");
  }

  @Override
  public Object convertInternalToJsonValue(Object internalValue) {
    throw new UnsupportedOperationException("TODO");
  }
}
