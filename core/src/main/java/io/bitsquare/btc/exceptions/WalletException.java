/*
 * This file is part of bisq.
 *
 * bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bitsquare.btc.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WalletException extends Exception {
    private static final long serialVersionUID = 4782095361220152803L;
    private static final Logger log = LoggerFactory.getLogger(WalletException.class);

    public WalletException(Throwable t) {
        super(t);
    }
}
