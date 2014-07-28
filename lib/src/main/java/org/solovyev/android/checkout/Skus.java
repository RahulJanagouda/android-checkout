/*
 * Copyright 2014 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.checkout;

import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * List of SKUs
 */
@Immutable
public final class Skus {

	/**
	 * Product type
	 */
	@Nonnull
	public final String product;

	@Nonnull
	public final List<Sku> list;

	Skus(@Nonnull String product, @Nonnull List<Sku> list) {
		this.product = product;
		this.list = Collections.unmodifiableList(list);
	}

	@Nonnull
	static Skus fromBundle(@Nonnull Bundle bundle, @Nonnull String product) throws RequestException {
		final List<String> list = bundle.getStringArrayList("DETAILS_LIST");

		final List<Sku> skus = new ArrayList<Sku>(list.size());
		for (String response : list) {
			try {
				final JSONObject object = new JSONObject(response);
				final String sku = object.getString("productId");
				final String price = object.getString("price");
				final String title = object.getString("title");
				final String description = object.getString("description");
				skus.add(new Sku(product, sku, price, title, description));
			} catch (JSONException e) {
				throw new RequestException(e);
			}

		}
		return new Skus(product, skus);
	}

	@Nullable
	public Sku getSku(@Nonnull String sku) {
		for (Sku s : list) {
			if (s.id.equals(sku)) {
				return s;
			}
		}
		return null;
	}

	public boolean hasSku(@Nonnull String sku) {
		return getSku(sku) != null;
	}
}