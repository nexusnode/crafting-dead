/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.immerse.client.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class LoggingErrorHandler implements ErrorHandler {

  public static final LoggingErrorHandler INSTANCE = new LoggingErrorHandler();

  private static final Logger logger = LogManager.getLogger();

  private LoggingErrorHandler() {}

  @Override
  public void warning(SAXParseException exception) throws SAXException {
    logger.warn(exception);
  }

  @Override
  public void error(SAXParseException exception) throws SAXException {
    logger.error(exception);

  }

  @Override
  public void fatalError(SAXParseException exception) throws SAXException {
    logger.fatal(exception);
  }
}
